package com.cylan.smart.plugin.ui.setting

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.base.dialog.InfoDialog
import com.cylan.smart.base.storage
import com.cylan.smart.plugin.ui.BaseActivity
import com.cylan.smart.plugin.R
import kotlinx.android.synthetic.main.home_setting_acitivity.*

/**
 * @author Lupy create on 19-1-14
 * @Description
 */

@Route(path = "/home/setting")
class SettingActivity : BaseActivity() {
    override fun useCommonTitle(): Boolean = true

    override fun layoutRes(): Int = R.layout.home_setting_acitivity

    override fun initView() {
        titleTv?.text = "设置"
        about.setOnClickListener {
            ARouter.getInstance().build("/home/about").navigation(this)
        }
        if (ConstantField.AppProfession.DOOR_GUARD.value ==
            storage?.getInt(ConstantField.BRANDS_SELECT, 0)
        ) {
            kaoqin_setting.visibility = View.VISIBLE
            kaoqin_setting.setOnClickListener {
                ARouter.getInstance().build("/home/kaoqin_setting").navigation(this)
            }
        }
        logout.setOnClickListener {
            InfoDialog.showDialog(this@SettingActivity,
                "退出不会删除账号信息，可以再次登录",
                posText = "退出登录", posClick = {
                    ARouter.getInstance()
                        .build("/home/main")
                        .withString("finish", "finish")
                        .navigation()
                    finish()
                })
        }

    }
}