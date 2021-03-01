package com.cylan.smart.plugin.ui.home

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.ui.BaseActivity


var globalShopId: String? = null
var globalBrandId: String? = null

/**
 * 首页架子
 */
@Route(path = "/home/accesscontrol", priority = 0, name = "门禁主页")
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun layoutRes(): Int =R.layout.home_main_activity

    override fun initView() {
    }

    override fun useCommonTitle(): Boolean = false


}
