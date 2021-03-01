package com.cylan.smart.plugin.ui.doorrecord

import com.alibaba.android.arouter.facade.annotation.Route
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.ui.BaseActivity

/**
 * @author Lupy
 * @since 2019/8/18
 */
@Route(path = "/home/generator")
class GeneratorActivity :BaseActivity() {
    override fun layoutRes(): Int  = R.layout.home_generator_activity_layout

    override fun initView() {
        titleTv?.text = "生成考勤"
    }

    override fun useCommonTitle(): Boolean = true


}