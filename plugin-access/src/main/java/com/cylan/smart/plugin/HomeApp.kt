package com.cylan.smart.plugin

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.cylan.smart.base.CrashHandler
import com.cylan.smart.base.utils.AppContext

/**
 * @author Lupy create on 19-2-12
 * @Description 单独调试Home模块
 */

class HomeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
        AppContext.init(this)
        CrashHandler.getInstance(null).init(this)
    }
}