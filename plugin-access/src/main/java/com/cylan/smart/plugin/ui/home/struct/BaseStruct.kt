package com.cylan.smart.plugin.ui.home.struct

import android.content.Context
import android.widget.RadioGroup
import com.cylan.smart.plugin.ui.home.fragment.base.BaseFragment


/**
 *@author Lupy Create on 2019-08-08.
 *@description 主页结构基类接口
 */

interface BaseStruct {

    /**
     * 获取中间靠左fragment
     */
     fun getMiddleLeftFragment():BaseFragment

    /**
     * 获取中间靠右
     */
     fun getMiddleRightFragment():BaseFragment


    /**
     * 初始化底部button
     */
    fun initNavigationBar(group:RadioGroup)

    /**
     * 刷新内容
     */
    fun refreshContent()

}