package com.cylan.smart.base.service.impl

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.alibaba.android.arouter.facade.annotation.Route
import com.cylan.smart.base.service.INetStateService
import com.cylan.smart.base.utils.AppBackgroundChecker
import com.cylan.smart.base.utils.AppContext
import java.util.concurrent.CopyOnWriteArrayList

/**
 *
 * @author yanzhendong
 * @since 2019/1/10 下午2:18
 */
@Suppress("unused")
@Route(path = "/base/net_state_service", name = "网络状态监视服务")
class NetStateServiceImpl : INetStateService {
    private lateinit var mNetStateReceiver: NetStateReceiver
    private val mNetStateObservers = CopyOnWriteArrayList<INetStateService.NetStateObserver>()
    override fun init(context: Context) {
        AppBackgroundChecker.getInstance().init(context.applicationContext as Application)
        mNetStateReceiver = NetStateReceiver()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(mNetStateReceiver, intentFilter)
    }

    override fun addNetStateObserver(observer: INetStateService.NetStateObserver) {
        mNetStateObservers.addIfAbsent(observer)
    }

    override fun removeNetStateObserver(observer: INetStateService.NetStateObserver) {
        mNetStateObservers.remove(observer)
    }

    private fun notifyNetStateChanged(connected: Boolean) {
        for (netStateObserver in mNetStateObservers) {
            netStateObserver.onNetStateChanged(
                connected,
                getNetType(AppContext.getContext()),
                AppBackgroundChecker.getInstance().isBackground
            )
        }
    }

    fun getNetType(context: Context): Int {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        if (networkInfo != null) {
            return networkInfo.type
        }
        return -1
    }

    inner class NetStateReceiver : BroadcastReceiver() {
        private var mConnected = false
        override fun onReceive(context: Context, intent: Intent) {
            var connected = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities: NetworkCapabilities? = manager.getNetworkCapabilities(manager.activeNetwork)
                connected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
            } else {
                executePing()
            }
            if (mConnected != connected) {
                notifyNetStateChanged(connected)
            }
            mConnected = connected
        }

        private fun executePing() {
//            ShellUtils.execCmd(String.format("ping -c 1 %s", ""), false)
        }
    }

    companion object {
        private const val PING_ADDRESS = ""
    }
}