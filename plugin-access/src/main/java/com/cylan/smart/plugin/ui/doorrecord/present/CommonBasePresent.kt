package com.cylan.smart.plugin.ui.doorrecord.present

import android.content.Intent

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.cylan.smart.plugin.ui.BasePresent
import com.cylan.smart.plugin.ui.BaseView
import com.cylan.smart.plugin.ui.doorrecord.CommonListActivity

/**
 * @author Lupy
 * @since 2019/8/19
 * 公共列表中间件
 */
abstract class CommonBasePresent(baseView: CommonBaseView) : BasePresent<CommonBaseView>(baseView) {

    lateinit var activity: CommonListActivity

 /**
     * 绑定CommonListActivity
     */
    fun attach(activity: CommonListActivity) {
        this.activity = activity
    }

    abstract fun parseIntent(intent: Intent)

    /**
     * 从头开始加载
     */
    abstract fun reload()

    /**
     * 初始化RecyelerView
     */
    abstract fun initRecylerView(recyclerView: RecyclerView)

    /**
     * 初始化其他的view 标题之类的
     */
    abstract fun initViewCommon()

    /**
     * 初始化title
     */
    protected fun initTilte(title: String) {
        baseView.initPageTilte(title)
    }

    /**
     * 初始化右侧文字
     */
    protected fun initRightText(text: String, function: () -> Unit) {
       baseView.initPageRightText(text,function)
    }

    /**
     * 初始化右侧图标
     */
    protected fun initRightIcon(@DrawableRes iconRes: Int, function: () -> Unit) {
       baseView.initPageRightIcon(iconRes,function)
    }


}