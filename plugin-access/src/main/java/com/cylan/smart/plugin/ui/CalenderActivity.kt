package com.cylan.smart.plugin.ui

import android.app.Activity
import android.util.Log
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cylan.smart.base.moduleapi.IAccountService
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.help.toVisible
import kotlinx.android.synthetic.main.home_calender_activity.*
import kotlinx.android.synthetic.main.home_calenderlist_item.*


/**
 * @author Lupy create on 19-1-15
 * @Description 日历选择
 */
@Route(path = "/home/calender")
class CalenderActivity : BaseActivity() {


    override fun layoutRes(): Int = R.layout.home_calender_activity

    override fun initView() {
        val navigation = ARouter.getInstance().navigation(IAccountService::class.java)
        titleTv?.text = "选择日期"
        rightText?.apply {
            text = "完成"
            toVisible()
            setOnClickListener {
                var startTime = dpv_calendar.selectStartDay
                var endTime = dpv_calendar.selectEndDay
                if (!startTime.isNullOrEmpty() && !endTime.isNullOrEmpty()) {
                    intent.putExtra("begin", startTime)
                    intent.putExtra("end", endTime)
                    setResult(Activity.RESULT_OK, intent)
                }
                finish()
            }
            dpv_calendar.post {
                var count = dpv_calendar.adapter?.itemCount ?: 12
                dpv_calendar.smoothScrollToPosition(count - 1)
            }
        }

    }

    override fun useCommonTitle(): Boolean = true

}