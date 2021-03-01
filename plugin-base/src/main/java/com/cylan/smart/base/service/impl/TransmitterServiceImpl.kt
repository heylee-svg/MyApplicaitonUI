package com.cylan.smart.base.service.impl

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cylan.smart.base.constant.TransmitterHeader
import com.cylan.smart.base.service.INetStateService
import com.cylan.smart.base.service.ITransmitterService
import com.cylan.smart.base.utils.CommandTranslator
import com.cylan.smart.base.utils.AppExecutor
import okio.ByteString
import java.io.IOException
import java.net.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 *
 * @author yanzhendong
 * @since 2019/1/10 上午9:16
 */
@Suppress("unused")
@Route(path = "/base/transmitter_service", name = "局域网消息发送接收服务")
class TransmitterServiceImpl : ITransmitterService, INetStateService.NetStateObserver {


    private var mTransmitterSocket: MulticastSocket? = null
    private val mTransmitterObservers = CopyOnWriteArrayList<ITransmitterService.TransmitterObserver>()
    private val mTransmitterPeersMap = ConcurrentHashMap<String, TransmitterPeer>()

    private lateinit var mTransmitterWriteRunnable: Runnable
    private lateinit var mTransmitterReadRunnable: Runnable
    private val mWriterQueue: ArrayDeque<TransmitterCommand> = ArrayDeque()
    private val mReaderQueue: ArrayDeque<ByteString> = ArrayDeque()
    private val mReaderBuffer: ByteArray = ByteArray(READ_BUFFER_SIZE)
    private val mEmptyTransmitterHeader = TransmitterHeader(0, 0, "")
    /**
     * 向设备发送消息,通过 udp 的方式发送,如果获取不到当前 peer 的 IP 地址,发送请求会被
     * 缓存,等到获取到 peer 的时候再延迟发送
     *
     * @param peer
     * @param content
     */
    override fun transmit(peer: String, content: ByteString) {
        Log.i(TAG, "transmit peer:$peer,content:$content")
        val transmitterPeer = getTransmitterPeer(peer)
        //需要解析出 transmitter header 还是用空的 header?
        val command = TransmitterCommand(transmitterPeer, mEmptyTransmitterHeader, content)
        mWriterQueue.add(command)
        loopWriter()
    }

    override fun init(context: Context?) {
        try {
            val iNetStateService = ARouter.getInstance().navigation(INetStateService::class.java)
            if (iNetStateService != null) {
                iNetStateService.addNetStateObserver(this)
            }

            mTransmitterPeersMap[BROADCAST_PEER] =
                    TransmitterPeer(BROADCAST_PEER, BROADCAST_IP, BROADCAST_PORT, version = "")
            mTransmitterWriteRunnable = Runnable {
                while (isLanSocketAvailable() && !mWriterQueue.isEmpty()) {
                    val result = sendOneCommand()
                    Log.i(TAG, "looper writer ,success:$result")

                }
            }

            mTransmitterReadRunnable = Runnable {
                while (isLanSocketAvailable()) {
                    readOneCommand()
                }
            }

            //不要等到用的时候才创建,要一开始就要创建
            createLanSocket(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkPeer(peer: TransmitterPeer): TransmitterPeer? {
        if (TextUtils.isEmpty(peer.ip) || peer.port == 0) {
            return null
        }
        return peer
    }

    @Throws(IOException::class)
    private fun sendOneCommand(): Boolean {
        try {
            val command = mWriterQueue.peek() ?: return false
            val transmitterPeer = checkPeer(command.peer) ?: return false
            val byteArray = command.content.toByteArray()
            val address = InetAddress.getByName(transmitterPeer.ip)
            val packet = DatagramPacket(byteArray, byteArray.size, address, transmitterPeer.port)
            Log.i(
                TAG,
                "sendOneCommand:${transmitterPeer.peer},${transmitterPeer.ip},${transmitterPeer.port},${Arrays.toString(
                    byteArray
                )}"
            )
            getLanSocket().send(packet)
            mWriterQueue.poll()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "sendOneCommand error:${e.message}")
            return false
        }
    }

    @Throws(IOException::class)
    private fun readOneCommand(): Boolean {
        try {
            val packet = DatagramPacket(
                mReaderBuffer,
                READ_BUFFER_SIZE
            )
            getLanSocket().receive(packet)
            val bytes = packet.data
            val byteString = ByteString.of(bytes, 0, packet.length)
            val commandHeader = CommandTranslator.decodeHeader(byteString)
            val transmitterPeer = getTransmitterPeer(commandHeader.cid)

            //必要的信息,接收的每一个 command 都应该更新这些信息
            transmitterPeer.ip = packet.address.hostAddress
            transmitterPeer.port = packet.port
            transmitterPeer.lastActive = System.currentTimeMillis()

            val transmitterCommand = TransmitterCommand(transmitterPeer, commandHeader, byteString)
            transmitForward(transmitterCommand)
            Log.i(
                TAG,
                "readOneCommand:${packet.address.hostAddress},${packet.port},header:$commandHeader,bytes:$byteString"
            )
        } catch (e: IOException) {
            Log.e(TAG, "readOneCommand error:${e.message}")
            return false
        }
        return true
    }

    /**
     * 反向将设备发送过来的数据透传给 APP
     *
     * @param command 设备端发送过来的数据
     */
    private fun transmitForward(
        command: TransmitterCommand
    ) {
        val transmitterHeader = command.header
        for (transmitterObserver in mTransmitterObservers) {
            transmitterObserver.onTransmitter(transmitterHeader, command.content)
        }
    }

    private fun initReaderAndWriter() {
        runReader()
        if (!mWriterQueue.isEmpty()) {
            loopWriter()
        }
    }

    private fun runReader() {
        AppExecutor.executeOnDiskIO(mTransmitterReadRunnable)
    }

    private fun loopWriter() {
        AppExecutor.executeOnDiskIO(mTransmitterWriteRunnable)
    }

    /**
     * 获取局域网发送消息的 socket
     *
     * @return 返回局域网发送数据的 socket
     */
    private fun getLanSocket(): DatagramSocket {
        return mTransmitterSocket ?: createLanSocket(false)
    }

    private fun isLanSocketAvailable(): Boolean {
        return mTransmitterSocket != null && !mTransmitterSocket!!.isClosed
    }

    /**
     * 创建局域网 socket
     *
     *@param recreate 是否重新创建 socket 如果为 true, 则之前已经创建的 socket 会把释放掉
     */
    private fun createLanSocket(recreate: Boolean): DatagramSocket {
        if (recreate && isLanSocketAvailable()) {
            mTransmitterSocket!!.close()
        }

        if (!isLanSocketAvailable()) {
            synchronized(this) {
                if (!isLanSocketAvailable()) {
                    try {
                        mTransmitterSocket = MulticastSocket(LISTEN_PORT)
                        mTransmitterSocket!!.reuseAddress = true
                        Log.i(TAG, "create lan socket success")
                        initReaderAndWriter()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e(TAG, "createLanSocket error: ${e.message}")
                    }
                }
            }
        }
        return mTransmitterSocket!!
    }

    /**
     * 获取和服务器连接的 socket
     *
     * @return 返回给服务器发送数据的 socket
     */
    private fun getWanSocket(): Socket {
        return Socket()
    }

    /**
     * 添加监听设备发送过来的消息的观察者
     *
     * @param observer 设备消息观察者
     */
    override fun addTransmitterObserver(observer: ITransmitterService.TransmitterObserver) {
        mTransmitterObservers.addIfAbsent(observer)
    }

    /**
     * 移除设备消息观察者
     *
     * @param observer 设备消息观察者
     */
    override fun removeTransmitterObserver(observer: ITransmitterService.TransmitterObserver) {
        mTransmitterObservers.remove(observer)
    }

    private fun getTransmitterPeer(peer: String): TransmitterPeer {
        return mTransmitterPeersMap[peer] ?: TransmitterPeer(peer).apply { mTransmitterPeersMap[peer] = this }
    }

    override fun transmit(content: ByteString) {
        transmit(BROADCAST_PEER, content)
    }

    override fun getTransmitterIp(peer: String): String? {
        return getTransmitterPeer(peer).ip
    }

    override fun getTransmitterPort(peer: String): Int {
        return getTransmitterPeer(peer).port
    }

    override fun onNetStateChanged(connected: Boolean, netType: Int, isBackground: Boolean) {

    }

    private class TransmitterPeer(
        val peer: String,
        var ip: String? = "",
        var port: Int = 0,
        var os: Int = 0,
        var version: String? = "",
        var lastActive: Long = 0
    )

    private class TransmitterCommand(
        val peer: TransmitterPeer,
        val header: TransmitterHeader,
        val content: ByteString
    )

    companion object {
        private const val TAG = "TransmitterServiceImpl"
        private const val BROADCAST_PEER = "broadcast_peer"
        private const val BROADCAST_IP = "255.255.255.255"
        private const val BROADCAST_PORT = 10008
        private const val LISTEN_PORT = 10009
        private const val READ_BUFFER_SIZE = 2048
    }
}