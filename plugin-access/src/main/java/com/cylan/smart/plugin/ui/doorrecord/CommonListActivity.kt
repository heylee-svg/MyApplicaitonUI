package com.cylan.smart.plugin.ui.doorrecord

import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.help.LoadingAndFailLayoutHelper
import com.cylan.smart.plugin.ui.BaseActivity
import com.cylan.smart.plugin.ui.BaseView
import com.cylan.smart.plugin.ui.doorrecord.present.CommonBasePresent
import com.cylan.smart.plugin.ui.doorrecord.present.CommonBaseView
import kotlinx.android.synthetic.main.home_common_list_layout.*

/**
 * @author Lupy
 * @since 2019/8/18
 * 公共listview页面 用于展示列表列的干净页面
 */
class CommonListActivity : BaseActivity(), CommonBaseView {

    lateinit var loadHelper: LoadingAndFailLayoutHelper<RelativeLayout>
    var commonBasePresent: CommonBasePresent? = null
    override fun layoutRes(): Int = R.layout.home_common_list_layout

    override fun initView() {
        var type = intent.getSerializableExtra("TYPE") as CommonPresentFactory.PresentType
        loadHelper = LoadingAndFailLayoutHelper(homeCommonContent)
        commonBasePresent = CommonPresentFactory.getPresentByType(this, type)
        if (commonBasePresent == null) {
            Log.e("lupy", "查找不到相应的中间件")
            finish()
        }
        commonBasePresent?.attach(this)
        commonBasePresent?.parseIntent(intent)
        commonBasePresent?.initViewCommon()
        commonBasePresent?.initRecylerView(commonList)
        commonBasePresent?.loadData()

    }


    override fun useCommonTitle(): Boolean = true


    override fun loadBegin() {
        loadHelper.loadBegin()
    }

    override fun loadSuccess() {
        loadHelper.loadSuccess()
    }

    override fun loadFails() {
//        loadHelper.loadFails(View.OnClickListener {
//            commonBasePresent?.loadData()
//        })
        loadHelper.loadNoData()
    }

    override fun netError() {
        loadHelper.netFails(View.OnClickListener {
            commonBasePresent?.loadData()
        })
    }

    override fun initPageTilte(title: String) {
        initTilte(title)
    }

    override fun initPageRightText(text: String, function: () -> Unit) {
       initRightText(text,function)
    }

    override fun initPageRightIcon(iconRes: Int, function: () -> Unit) {
       initRightIcon(iconRes,function)
    }
}