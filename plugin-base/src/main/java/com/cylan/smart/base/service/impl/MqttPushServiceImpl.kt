package com.cylan.smart.base.service.impl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.cylan.smart.base.MQTTService
import com.cylan.smart.base.RetrofitCore
import com.cylan.smart.base.service.IPushService
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 *
 * @author yanzhendong
 * @since 2019/1/9 下午7:10
 */
@Suppress("unused")
@Route(path = "/base/mqtt_push_service", name = "服务器Mqtt推送服务")
class MqttPushServiceImpl : IPushService, ServiceConnection {
    override fun onServiceDisconnected(name: ComponentName?) {
        Log.e("AAAAA", "onServiceDisconnected")
    }
    private var mqttService: MQTTService? = null

    override fun onServiceConnected(name: ComponentName?, service: IBinder) {
        Log.e("AAAAA", "onServiceConnected")
        val customBinder = service as MQTTService.CustomBinder
        mqttService = customBinder.service
        subscribeInternal()
    }

    override fun addServerPushObserver(topic: String, observer: IPushService.ServerPushObserver) {
        val arrayList =
            mPendingSubscribeList[topic] ?: CopyOnWriteArrayList<IPushService.ServerPushObserver>().apply {
                mPendingSubscribeList[topic] = this
            }
        arrayList.addIfAbsent(observer)
        subscribeInternal()
    }

    private fun subscribeInternal() {
        mqttService?.apply {
            mPendingSubscribeList.forEach {
                for (serverPushObserver in it.value) {
                    subscribe(it.key, serverPushObserver)
                }
            }
            mPendingSubscribeList.clear()
        }
    }

    override fun removeServerPushObserver(topic: String, observer: IPushService.ServerPushObserver) {
        mqttService?.apply {
            mPendingSubscribeList[topic]?.remove(observer)
            unsubscribe(topic, observer)
        }
    }

    private val mPendingSubscribeList =
        ConcurrentHashMap<String, CopyOnWriteArrayList<IPushService.ServerPushObserver>>()

    override fun init(context: Context) {
        Log.e("AAAAA", "---------------------------")
        val serviceIntent = Intent(context, MQTTService::class.java)
        context.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
    }
}