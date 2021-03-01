package com.cylan.smart.plugin.adapter

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.cylan.smart.base.BaseQuickAdapter
import com.cylan.smart.plugin.R
import java.util.*

/**
 * @author Lupy
 * @since 2019/8/18
 */
class MonthAdapter(context: Context,var itemCallBack:(month:Int)->Unit = {}) : BaseQuickAdapter<String>(
    context,
    arrayListOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
) {


    var currentMonth:Int
    var selectMonth:Int

    init {
        currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        selectMonth = currentMonth
    }

    override fun itemLayout(): Int = R.layout.home_month_list_item_layout

    override fun convert(helper: ViewHolderHelper, t: String) {
        helper.setText(R.id.monthTv, t)
        if(helper.pos == selectMonth){
            helper.itemView.findViewById<TextView>(R.id.monthTv).setBackgroundResource(R.mipmap.select_day_bg)
        }else{
            helper.itemView.findViewById<TextView>(R.id.monthTv).setBackgroundColor(Color.WHITE)
        }

        helper.itemView.setOnClickListener {
            if(selectMonth == helper.pos){
                return@setOnClickListener
            }
            var temp = selectMonth
            selectMonth = helper.pos
            notifyItemChanged(temp)
            notifyItemChanged(selectMonth)
            itemCallBack.invoke(selectMonth+1)
        }
    }
}