package com.cylan.smart.base.utils

import android.app.Activity
import android.util.DisplayMetrics

object ScreenUtils{

    /**
     * 获取屏幕宽度
     */

        fun getWidthPixels(activity: Activity): Int {
            val dm = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(dm)
            return dm.widthPixels
        }


}