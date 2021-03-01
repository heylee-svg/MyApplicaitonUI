package com.cylan.smart.base.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.cylan.smart.base.constant.TransmitterHeader
import okio.ByteString

/**
 * 实验性功能,接口可能随时会改动,慎重使用
 *
 * @author yanzhendong
 * @since 2019/1/10 上午9:15
 */
@Suppress("unused")
interface ITransmitterService : IProvider {

    /**
     * 向设备发送消息,通过 udp 的方式发送,如果获取不到当前 peer 的 IP 地址,发送请求会被
     * 缓存,等到获取到 peer 的时候再延迟发送
     *
     * @param peer 需要发送的设备
     * @param content 需要发送给设备的内容
     */
    fun transmit(peer: String, content: ByteString)

    /**
     * 向设备发送消息,通过udp 广播的方式发送
     *
     * @param content 需要广播发送的内容
     */
    fun transmit(content: ByteString)

    /**
     * 获取设备的局域网 IP 地址
     *
     * @param peer 设备的cid
     * @return 设备的局域网地址,找不到设备对应的信息返回空或者 null
     */
    fun getTransmitterIp(peer: String): String?

    /**
     * 获取设备的局域网端口号
     *
     * @param peer 设备的 cid
     * @return 设备的局域网端口号,找不到设备对应的信息返回0
     */
    fun getTransmitterPort(peer: String): Int

    /**
     * 添加监听设备发送过来的消息的观察者
     *
     * @param observer 设备消息观察者
     */
    fun addTransmitterObserver(observer: TransmitterObserver)

    /**
     * 移除设备消息观察者
     *
     * @param observer 设备消息观察者
     */
    fun removeTransmitterObserver(observer: TransmitterObserver)

    interface TransmitterObserver {

        /**
         * 监听设备发送消息的接口
         *
         * @param header  设备发送的消息头
         * @param content 设备发送的字节数据
         */
        fun onTransmitter(header: TransmitterHeader, content: ByteString)
    }
}