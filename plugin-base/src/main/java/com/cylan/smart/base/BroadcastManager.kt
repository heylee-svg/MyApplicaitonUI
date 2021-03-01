package com.cylan.smart.base

import android.app.Application
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cylan.smart.base.utils.AppContext

/**
 * @author Lupy create on 19-1-22
 * @Description 广播管理
 */

class BroadcastManager {

    var context: Application
    var localManager: LocalBroadcastManager

    init {
        context = AppContext.getContext()
        localManager = LocalBroadcastManager.getInstance(context)
    }

    companion object {
        val instance: BroadcastManager by lazy {
            BroadcastManager()
        }
    }


    /**
     * 发送ｔｏｋｅｎ过期广播
     */
    fun sendAuthTokeninvalidate() {

        localManager.sendBroadcast(Intent("com.cylan.smart.token").putExtra("type", 0))
    }

    /**
     * 密码修改广播
     */
    fun passwordChange() {
        localManager.sendBroadcast(Intent("com.cylan.smart.token").putExtra("type", 1))
    }

    /**
     * 账号已停用
     */
    fun accountInvalidate(){
        localManager.sendBroadcast(Intent("com.cylan.smart.token").putExtra("type", 2))
    }
}