package com.cylan.smart.plugin.ui.home.fragment.kaoqin

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.base.dialog.TipsDialog
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.adapter.MonthAdapter
import com.cylan.smart.plugin.data.DataManger
import com.cylan.smart.plugin.data.bean.KaoqinTotal
import com.cylan.smart.plugin.ui.doorrecord.CommonListActivity
import com.cylan.smart.plugin.ui.doorrecord.CommonPresentFactory
import com.cylan.smart.plugin.ui.home.fragment.base.BaseFragment
import com.cylan.smart.plugin.widget.CircleProgressView
import java.util.*

/**
 * @author Lupy
 * @since 2019/8/18
 * 当月考勤fragment
 */

class MonthKaoqinFragment : BaseFragment(), View.OnClickListener {


    lateinit var monthList: RecyclerView
    lateinit var currentDayTv: TextView
    lateinit var normalNumber :TextView
    lateinit var unnormalNumber :TextView
    lateinit var total_desc :TextView
    lateinit var yearTv: TextView
    lateinit var circleProgress: CircleProgressView
    var calender: Calendar
    var currenMonth: Int
    var data :KaoqinTotal?=null
    var currentYear :Int =Calendar.YEAR

    init {
        calender = Calendar.getInstance()
        currenMonth = calender.get(Calendar.MONTH) + 1
        currentYear =calender.get(Calendar.YEAR)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.home_month_kaoqin_layout, container, false)
        monthList = view.findViewById(R.id.monthList)
        currentDayTv = view.findViewById(R.id.currentDayTv)
        normalNumber=view.findViewById(R.id.normalNumber)
        unnormalNumber=view.findViewById(R.id.unNormalNumber)
        total_desc=view.findViewById(R.id.total_desc)
        yearTv = view.findViewById(R.id.yearTv)
        circleProgress = view.findViewById(R.id.circleProgress)
        view.findViewById<ImageView>(R.id.prevBtn).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.nextBtn).setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMonthKaoqinData( "${calender.get(Calendar.YEAR)}-${currenMonth}")
        yearTv.text = "${calender.get(Calendar.YEAR)}"
        currentDayTv.text = "${calender.get(Calendar.YEAR)}-${String.format("%02d", currenMonth)}"
        monthList.layoutManager = object : GridLayoutManager(context, 4) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        monthList.adapter = MonthAdapter(context!!) {
            currenMonth = it
            currentDayTv.text = "${calender.get(Calendar.YEAR)}-${String.format("%02d", it)}"
            getMonthKaoqinData("${calender.get(Calendar.YEAR)}-${String.format("%02d", it)}")
        }


        getView()
            ?.findViewById<TextView>(R.id.checkDetails)
            ?.setOnClickListener {
                var intent = Intent(context,CommonListActivity::class.java)
                intent.putExtra("TYPE",CommonPresentFactory.PresentType.KAOQINLIST)
                intent.putExtra("PARAMETER_1","${calender.get(Calendar.YEAR).toString()}-${String.format("%02d", currenMonth).toString()}")
                it.context.startActivity(intent)
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.prevBtn -> {
                calender.add(Calendar.YEAR, -1)
                yearTv.text = "${calender.get(Calendar.YEAR)}"
                currentDayTv.text = "${calender.get(Calendar.YEAR)}-${String.format("%02d", currenMonth)}"
                getMonthKaoqinData("${calender.get(Calendar.YEAR)}-${String.format("%02d", currenMonth)}")
            }
            R.id.nextBtn -> {
                if(calender.get(Calendar.YEAR)<currentYear){
                    calender.add(Calendar.YEAR, 1)
                    yearTv.text = "${calender.get(Calendar.YEAR)}"
                    currentDayTv.text = "${calender.get(Calendar.YEAR)}-${String.format("%02d", currenMonth)}"
                    getMonthKaoqinData("${calender.get(Calendar.YEAR)}-${String.format("%02d", currenMonth)}")
                }
            }
        }
    }

    override fun OnShopIdOrBrandIdChange() {

    }
    fun updateData(){
        circleProgress.maxPro = data?.should_number?:0
        circleProgress.prog = data?.really_number?:0
        normalNumber.text=data?.normal_number?.toString()?:"0"
        unnormalNumber.text=data?.abnormal_number?.toString()?:"0"

        var shouldNumber :String=data?.should_number?.toString()?:"0"
        var reallyNumber :String =data?.really_number?.toString()?:"0"
        total_desc.text=contactBuilder(shouldNumber,reallyNumber)
    }

    fun getMonthKaoqinData(month: String) {
        DataManger.getInstance()
            .getMonthKaoqinInfo(month) { response, status ->
                if (status == ConstantField.SUCCESS) {
                    data=response

                }else{
                    data =null

                }
                updateData()

            }
    }
    fun contactBuilder(parms1:String,parms2:String) :SpannableStringBuilder{
        val spannable = SpannableStringBuilder()
        spannable.append("应出勤: ").setSpan(
            ForegroundColorSpan(resources.getColor(R.color.text_636363)),
            0,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        var startIndex=spannable.length
        spannable.append(parms1).setSpan(
            ForegroundColorSpan(resources.getColor(R.color.text_3CDEC2)),
            startIndex,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        startIndex=spannable.length
        spannable.append(" 人次 实出勤: ").setSpan(
            ForegroundColorSpan(resources.getColor(R.color.text_666666)),
            startIndex,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        startIndex=spannable.length
        spannable.append(parms2).setSpan(
            ForegroundColorSpan(resources.getColor(R.color.text_3CDEC2)),
            startIndex,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        startIndex=spannable.length
        spannable.append(" 人次").setSpan(
            ForegroundColorSpan(resources.getColor(R.color.text_666666)),
            startIndex,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        return spannable
    }
}