package com.cylan.smart.base.service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * 服务器推送通知的管理服务
 * @author yanzhendong
 * @since 2019/1/9 下午7:09
 */
@Suppress("unused")
interface IPushService : IProvider {

    fun addServerPushObserver(topic: String, observer: ServerPushObserver)

    fun removeServerPushObserver(topic: String, observer: ServerPushObserver)

    interface ServerPushObserver {

        fun onReceivePush(topic: String, message: String)
    }
}