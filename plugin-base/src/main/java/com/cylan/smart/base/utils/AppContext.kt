package com.cylan.smart.base.utils

import android.app.Application

/**
 *
 * @author yanzhendong
 * @since 2019/1/11 上午10:02
 */
@Suppress("unused")
object AppContext {
    private lateinit var application: Application
    fun getContext(): Application {
        return application
    }

    fun init(application: Application) {
        this.application = application
    }
}