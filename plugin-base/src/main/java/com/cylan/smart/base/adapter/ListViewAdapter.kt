package com.cylan.smart.base.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cylan.smart.base.BaseQuickAdapter
import com.cylan.smart.base.R


class ListViewAdapter(context: Context, list:List<String>) :
    BaseQuickAdapter<String>(context,list){


    override fun itemLayout(): Int = R.layout.list_item_view

    override fun convert(helper: ViewHolderHelper, t: String) {
        helper.setText(R.id.text,t)
    }
}