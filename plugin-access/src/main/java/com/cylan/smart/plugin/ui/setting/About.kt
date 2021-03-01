package com.cylan.smart.plugin.ui.setting

import com.alibaba.android.arouter.facade.annotation.Route
import com.cylan.smart.plugin.BuildConfig
import com.cylan.smart.plugin.ui.BaseActivity
import com.cylan.smart.plugin.R
import kotlinx.android.synthetic.main.home_about_activity.*

/**
 * @author Lupy create on 19-1-14
 * @Description　关于
 */

@Route(path = "/home/about")
class About : BaseActivity() {
    override fun useCommonTitle(): Boolean = true

    override fun layoutRes(): Int = R.layout.home_about_activity
    override fun initView() {
        titleTv?.text = "关于"
        appVersion.text = packageManager.getPackageInfo(packageName, 0)?.versionName ?: ""

        if("china".equals(BuildConfig.FLAVOR)){
            logoIcon.setImageResource(R.drawable.icon_smartapp)
        }else{
            logoIcon.setImageResource(R.drawable.cylan_logo)
        }

    }
}