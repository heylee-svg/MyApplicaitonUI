package com.cylan.smart.plugin.ui.doorrecord.present

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.base.storage
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.adapter.KaoQinListAdapter
import com.cylan.smart.plugin.data.DataManger
import com.cylan.smart.plugin.data.bean.KaoqinInfo
import com.cylan.smart.plugin.ui.BaseView
import com.cylan.smart.plugin.widget.RecyclerItemDocor

/**
 * @author Lupy
 * @|since 2019/8/19
 * 考勤列表 中间件
 */
class KaoQinListPresent(baseView: CommonBaseView) : CommonBasePresent(baseView) {
    var list :ArrayList<KaoqinInfo>? = arrayListOf()
    var month :String =""
    lateinit var mAdapter :KaoQinListAdapter

    override fun parseIntent(intent: Intent) {

    month= intent?.getStringExtra("PARAMETER_1")

    }

    override fun initRecylerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(RecyclerItemDocor(activity, 10f))
        mAdapter =KaoQinListAdapter(activity, list!!)
        mAdapter.month=month
        recyclerView.adapter = mAdapter
        //暂时test
        baseView.loadSuccess()
    }

    override fun reload() {

    }

    override fun loadData() {

        getMonthDetailKaoqinData(month)
    }

    override fun initViewCommon() {
        initTilte("${storage?.getString(ConstantField.SHOP_NAME,"")} ${month.split("-")[1]}月份考勤")
//        initRightIcon(R.drawable.generator_kaoqin_icon) {
//            ARouter.getInstance().build("/home/generator").navigation(activity)
//        }
    }

    fun getMonthDetailKaoqinData(month: String) {
        DataManger.getInstance()
            .getKaoqinListByMonth(month) { response, status ->
                if (status == ConstantField.SUCCESS) {
                list?.addAll(response?.datas ?: arrayListOf())
                    mAdapter.notifyDataSetChanged()
                } else {
                    baseView.loadFails()
                }

            }
    }

}