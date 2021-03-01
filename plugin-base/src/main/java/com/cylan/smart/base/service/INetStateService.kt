package com.cylan.smart.base.service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * 网络状态监视服务
 * @Experimental
 *
 * @author yanzhendong
 * @since 2019/1/10 下午2:17
 */

@Suppress("unused")
interface INetStateService : IProvider {

    fun addNetStateObserver(observer: NetStateObserver)

    fun removeNetStateObserver(observer: NetStateObserver)

    interface NetStateObserver {
        fun onNetStateChanged(connected: Boolean, netType: Int, isBackground: Boolean)
    }
}