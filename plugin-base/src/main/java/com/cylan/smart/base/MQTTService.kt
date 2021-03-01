package com.cylan.smart.base

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Binder
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import com.cylan.smart.base.service.IPushService
import com.cylan.smart.base.utils.AppExecutor
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class MQTTService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return CustomBinder()
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("AAAAA", "MQTT Service Created")
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttAndroidClient.unregisterResources()
    }

    private lateinit var mqttAndroidClient: MqttAndroidClient

    private lateinit var connectOptions: MqttConnectOptions

    private val callbacks = ConcurrentHashMap<String, CopyOnWriteArrayList<IPushService.ServerPushObserver>>()
    private val pendingCallbacks = ConcurrentHashMap<String, CopyOnWriteArrayList<IPushService.ServerPushObserver>>()

    private fun init() {
        var hostString = storage?.getString("HOST", OEM.getInstance().host)
        var host = if (TextUtils.isEmpty(hostString)) OEM.getInstance().host else hostString!!
        host = host.replace("http://", "").replace("https://", "")
        Log.e("AAAAA", "host :$host")
        mqttAndroidClient =
            MqttAndroidClient(this, "tcp://$host:1883", "SmartAppAndroid-${System.currentTimeMillis()}")
        mqttAndroidClient.setCallback(mqttCallback)
        connectOptions = MqttConnectOptions()
        connectOptions.isCleanSession = true
        connectOptions.connectionTimeout = 10
        connectOptions.keepAliveInterval = 20
        doClientConnection()
    }

    private fun doClientConnection() {
        Log.e("AAAAA", "isConnected:${mqttAndroidClient.isConnected},isConnectIsNormal:${isConnectIsNormal()}")
        if (!mqttAndroidClient.isConnected && isConnectIsNormal()) {
            Log.e("AAAAA", "doClientConnection")
            mqttAndroidClient.connect(connectOptions, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.e("AAAAA", "MQTT Connect success")
                    for (pendingCallback in pendingCallbacks) {
                        for (pushObserver in pendingCallback.value) {
                            subscribe(pendingCallback.key, pushObserver)
                        }
                    }
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    exception?.printStackTrace()
                    reconnect()
                    Log.e("AAAAA", "MQTT Connect Error:${exception?.message}")
                }
            })
        }
    }

    private val mqttCallback: MqttCallback = object : MqttCallback {
        override fun messageArrived(topic: String, message: MqttMessage) {
            val string = String(message.payload)
            Log.e("AAAAA", "receive mqtt message:$string")
            callbacks[topic]?.forEach { it.onReceivePush(topic, string) }
        }

        override fun connectionLost(cause: Throwable?) {
            reconnect()
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {

        }

    }

    private var autoConnectRunnable = object : Runnable {
        override fun run() {
            AppExecutor.getMainThreadExecutor().removeCallbacks(this)
            doClientConnection()
        }
    }

    private fun reconnect() {
        AppExecutor.getMainThreadExecutor().postDelayed(autoConnectRunnable, 3000)
    }

    fun subscribe(topic: String, callback: IPushService.ServerPushObserver) {
        logger?.info("register mqtt key is ${topic}")
        val list = callbacks[topic] ?: CopyOnWriteArrayList<IPushService.ServerPushObserver>().apply {
            callbacks[topic] = this
        }
        list.addIfAbsent(callback)
        if (mqttAndroidClient.isConnected) {
            Log.e("AAAAA", "subscribe for topic:$topic")
            mqttAndroidClient.subscribe(topic, 0)
        } else {
            val pendingCallbacks: CopyOnWriteArrayList<IPushService.ServerPushObserver> = (pendingCallbacks[topic]
                ?: CopyOnWriteArrayList<IPushService.ServerPushObserver>().apply { pendingCallbacks[topic] = this })
            pendingCallbacks.addIfAbsent(callback)
        }
    }

    fun unsubscribe(topic: String, callback: IPushService.ServerPushObserver) {
        if (mqttAndroidClient.isConnected) {
            callbacks[topic]?.remove(callback)
            pendingCallbacks[topic]?.remove(callback)
            Log.e("AAAAA", "unsubscribe for topic:$topic")
            mqttAndroidClient.unsubscribe(topic)
        }
    }

    /** 判断网络是否连接 */
    private fun isConnectIsNormal(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivityManager.activeNetworkInfo
        return if (info != null && info.isAvailable) {
            val name = info.typeName;
            Log.i("AAAAA", "MQTT当前网络名称：$name")
            true
        } else {
            Log.i("AAAAA", "MQTT 没有可用网络")
            false
        }
    }

    inner class CustomBinder : Binder() {
        val service: MQTTService
            get() = this@MQTTService
    }

    interface IGetMessageCallBack {

        fun setMessage(message: String)

    }

}
