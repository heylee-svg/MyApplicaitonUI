package com.cylan.smart.plugin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.widget.CalanderView
import java.util.*

/**
 * @author Lupy create on 19-1-16
 * @Description 日历列表适配器
 */

class CalenderAdapter(context: Context) : RecyclerView.Adapter<CalenderAdapter.ViewHolder>() {

    var layoutInflater: LayoutInflater

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.home_calenderlist_item, p0, false))
    }

    override fun getItemCount(): Int {
        return 12
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        var ca = Calendar.getInstance()
        ca.add(Calendar.MONTH, p1 - 11)
        p0.mouthTitle.text = "${ca.get(Calendar.YEAR)}年${ca.get(Calendar.MONTH) + 1}月"
        p0.calander.setDate(ca)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mouthTitle: TextView
        var calander: CalanderView

        init {
            mouthTitle = itemView.findViewById(R.id.mouth_title) as TextView
            calander = itemView.findViewById(R.id.mouth_calender) as CalanderView
        }
    }
}