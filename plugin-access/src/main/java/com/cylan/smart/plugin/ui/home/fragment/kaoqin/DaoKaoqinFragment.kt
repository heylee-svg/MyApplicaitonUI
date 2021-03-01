package com.cylan.smart.plugin.ui.home.fragment.kaoqin

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.base.dialog.TipsDialog
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.adapter.KaoqinTotalAdapter
import com.cylan.smart.plugin.data.DataManger
import com.cylan.smart.plugin.data.bean.KaoqinInfo
import com.cylan.smart.plugin.ui.home.fragment.base.BaseFragment
import com.cylan.smart.plugin.widget.CalanderView
import java.util.*

/**
 * @author Lupy
 * @since 2019/8/18
 * 当日考勤fragment
 */
class DaoKaoqinFragment : BaseFragment() {

    lateinit var calanderView: CalanderView
    lateinit var currentDayTv: TextView
    lateinit var lateList: RecyclerView
    lateinit var leaveEalyList: RecyclerView
    lateinit var no_late_tips: TextView
    lateinit var no_early_tips: TextView

    private var lateListData = ArrayList<KaoqinInfo>()   //迟到
    private var leaveEalyListData = ArrayList<KaoqinInfo>()  //早退


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.home_day_kaoqin_layout, container, false)
        calanderView = view.findViewById(R.id.calander)
        currentDayTv = view.findViewById(R.id.currentDayTv)
        lateList = view.findViewById(R.id.lateList)
        leaveEalyList = view.findViewById(R.id.quiteEalyList)
        no_early_tips=view.findViewById(R.id.no_early_tips)
        no_late_tips=view.findViewById(R.id.no_late_tips)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calanderView.setDate(Calendar.getInstance())
        getDayKaoqinData(calanderView.getCurrentDate())
        calanderView.onItemClickListener = object : CalanderView.OnItemClickListener {
            override fun clickItem(year: Int, mouth: Int, day: Int, week: String) {
                currentDayTv.text =
                    "${year}-${String.format("%02d", mouth)}-${String.format("%02d", day)} ${week}"
                lateListData.clear()
                leaveEalyListData.clear()
                getDayKaoqinData("${year}-${String.format("%02d", mouth)}-${String.format("%02d",day)}")
            }
        }
        currentDayTv.text = calanderView.getCurrentDay()
        lateList.layoutManager = LinearLayoutManager(context)
        leaveEalyList.layoutManager = LinearLayoutManager(context)

        lateList.adapter = KaoqinTotalAdapter(context!!, lateListData)

        leaveEalyList.adapter = KaoqinTotalAdapter(context!!, leaveEalyListData)
    }

    override fun OnShopIdOrBrandIdChange() {

    }

    fun getDayKaoqinData(day: String) {
        DataManger.getInstance()
            .getDayKaoqinInfo(day) { response, status ->
                if (status == ConstantField.SUCCESS) {
                    if(response?.came_late_datas?.size==0) no_late_tips.visibility=View.VISIBLE else no_late_tips.visibility=View.GONE
                    if(response?.left_early_datas?.size==0)no_early_tips.visibility=View.VISIBLE else no_early_tips.visibility=View.GONE
                    lateListData.addAll(response?.came_late_datas?: arrayListOf())
                    leaveEalyListData.addAll(response?.left_early_datas?: arrayListOf())
                    lateList.adapter?.notifyDataSetChanged()
                    leaveEalyList.adapter?.notifyDataSetChanged()

                } else {
                  //  TipsDialog.showCommonTipsDialog(response?.msg ?: "", context!!)
                }

            }
    }
}