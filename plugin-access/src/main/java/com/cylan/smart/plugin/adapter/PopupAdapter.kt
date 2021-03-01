package com.cylan.smart.plugin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.cylan.smart.plugin.R

/**
 * @author Lupy
 * @since 2019/8/25
 */
class PopupAdapter(var context:Context) :BaseAdapter() {

    var layoutInflater:LayoutInflater

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.home_popup_list_item_layout,parent,false)
    }

    override fun getItem(position: Int): Any {
        return  position
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getCount(): Int {
        return 10
    }
}