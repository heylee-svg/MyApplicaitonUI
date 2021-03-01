package com.cylan.smart.plugin.ui.doorrecord.present

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.base.storage
import com.cylan.smart.plugin.adapter.PersonalKaoqinAdapter
import com.cylan.smart.plugin.data.DataManger
import com.cylan.smart.plugin.data.bean.KaoqinInfo
import com.cylan.smart.plugin.data.bean.KaoqinPersonInfo
import com.cylan.smart.plugin.data.bean.KaoqinState
import com.cylan.smart.plugin.data.bean.KaoqinTotal
import com.cylan.smart.plugin.ui.BaseView

/**
 * @author Lupy
 * @since 2019/8/19
 * 个人考勤中间件
 */
class PersonalKaoQinPresent(baseView: CommonBaseView) : CommonBasePresent(baseView) {
    var month: String = ""
    var personId: String = ""
    var personName :String =""
    var list: ArrayList<KaoqinState> = arrayListOf()
    private lateinit  var personInfo: KaoqinInfo
    var mAdapter: PersonalKaoqinAdapter? = null
    lateinit var recyclerView :RecyclerView


    override fun parseIntent(intent: Intent) {
        month = intent?.getStringExtra("PARAMETER_1")
        personId = intent?.getStringExtra("PARAMETER_2")

    }

    override fun reload() {

    }

    override fun initRecylerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        this.recyclerView=recyclerView
//        mAdapter = PersonalKaoqinAdapter(activity, personInfo, list
//        recyclerView.adapter = mAdapter
//        baseView.loadSuccess()
    }

    override fun initViewCommon() {

        initTilte("${storage?.getString(ConstantField.SHOP_NAME,"")} ${month.split("-")[1]}月份考勤")
    }

    override fun loadData() {
        getPersonalKaoqinByMonth(month, personId)
    }

    fun getPersonalKaoqinByMonth(
        month: String,
        personId: String
    ) {
        DataManger.getInstance()
            .getPersonalKaoqinByMonth(month, personId) { response, status ->
                if (status == ConstantField.SUCCESS) {
                    personInfo = KaoqinInfo(
                        0L,
                        "",  //人物标识
                        response?.person_info?.person_name ?: "",//人物姓名
                        response?.person_info?.gender ?: "",// 性别：male/female
                        response?.person_info?.job ?: "",//职位
                        response?.person_info?.face_url ?: "",//脸图地址
                        response?.person_info?.group ?: "",//脸库
                        0L, //上班时间
                        0L, //下班时间
                        0,//考勤状态
                        response?.state?.really_number ?: 0,//实到
                        response?.state?.should_number ?: 0,//应到
                        response?.state?.normal_number ?: 0,//正常考勤
                        response?.state?.abnormal_number ?: 0//异常考勤
                    )

                    list?.addAll(response?.datas ?: arrayListOf())
                    mAdapter = PersonalKaoqinAdapter(activity, personInfo, list)
                    recyclerView.adapter = mAdapter
                    baseView.loadSuccess()
                    mAdapter?.notifyDataSetChanged()
                } else {
                    baseView.loadFails()

                }

            }
    }


}