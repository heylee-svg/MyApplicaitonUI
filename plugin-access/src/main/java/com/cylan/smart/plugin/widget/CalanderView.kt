package com.cylan.smart.plugin.widget

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import com.cylan.smart.plugin.R
import kotlinx.android.synthetic.main.home_canlender_view_item.view.*
import java.util.*
import java.util.jar.Attributes
import kotlin.collections.ArrayList

/**
 * @author Lupy create on 19-1-16
 * @Description
 */
class CalanderView(context: Context, attributes: AttributeSet) : RecyclerView(context, attributes) {


    var nowDay: Int
    var selectDay: Int

    lateinit var adapterC: CalanderAdapter
    var setMonth: Int
    var setYear: Int
    var dateList: ArrayList<Int> = ArrayList<Int>()
    var days: Array<Int> = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    var weeks: Array<String> = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
    var onItemClickListener: OnItemClickListener? = null


    init {
        isNestedScrollingEnabled = false
        layoutManager = object : GridLayoutManager(context, 7) {
            fun canScrollVertically(d: Int) = false
        }
        nowDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        setMonth = Calendar.getInstance().get(Calendar.MONTH)
        setYear = Calendar.getInstance().get(Calendar.YEAR)
        selectDay = nowDay
    }


    fun setDate(calender: Calendar) {
        dateList.clear()
        setYear = calender.get(Calendar.YEAR)
        setMonth = calender.get(Calendar.MONTH)
        calender.set(Calendar.DAY_OF_MONTH, 1)
        var week = calender.get(Calendar.DAY_OF_WEEK) - 1
        for (w in 0..week) {
            dateList.add(-1)
        }
        dateList.removeAt(week)
        var dayNum = days[calender.get(Calendar.MONTH)]
        if (dayNum == 28) {
            var year = calender.get(Calendar.YEAR)
            if (year % 4 == 0) {
                dayNum = 29
            }
        }

        for (i in 1..dayNum) {
            dateList.add(i)
        }

        adapterC = CalanderAdapter(context)
        adapter = adapterC
    }


    inner class CalanderAdapter(context: Context) : Adapter<CalanderAdapter.ViewHolder>() {

        var layoutInflater: LayoutInflater

        init {
            layoutInflater = LayoutInflater.from(context)
        }


        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(layoutInflater.inflate(R.layout.home_canlender_view_item, p0, false))
        }

        override fun getItemCount(): Int {
            return dateList.size
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {
            var date = dateList.get(p1)
            if (date == -1) {
                viewHolder.date.text = ""
                viewHolder.date.setOnClickListener(null)
            } else {
                viewHolder.date.text = String.format("%02d", date)
                if (date == nowDay) {
                    viewHolder.date.setBackgroundResource(R.drawable.current_day_bg)
                } else {
                    if (date == selectDay) {
                        viewHolder.date.setBackgroundResource(R.drawable.select_day_bg)
                    } else {
                        viewHolder.date.setBackgroundColor(Color.WHITE)
                    }
                }
                viewHolder.itemView.setOnClickListener {
                    if (date != selectDay) {
                        var temp = selectDay
                        selectDay = date
                        notifyItemChanged(dateList.indexOf(selectDay))
                        notifyItemChanged(dateList.indexOf(temp))
                        onItemClickListener?.clickItem(
                            setYear,
                            setMonth + 1,
                            selectDay,
                            weeks[dateList.indexOf(selectDay) % 7]
                        )
                    }
                }
            }

        }


        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var date: TextView
            var container: LinearLayout

            init {
                container = itemView.findViewById(R.id.item_container)
                date = itemView.findViewById(R.id.date)
            }
        }

    }

    interface OnItemClickListener {
        fun clickItem(year: Int, mouth: Int, day: Int, week: String)
    }

    /**
     * 获取当前日期显示
     */
    fun getCurrentDay():String{
        return "${setYear}-${String.format("%02d",setMonth+1)}-${String.format("%02d",nowDay)} ${weeks[dateList.indexOf(nowDay) % 7]}"
    }
    fun getCurrentDate():String{
        return "${setYear}-${String.format("%02d",setMonth+1)}-${String.format("%02d",nowDay)}"
    }



}