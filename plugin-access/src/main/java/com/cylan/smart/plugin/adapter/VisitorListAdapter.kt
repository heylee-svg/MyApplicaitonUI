package com.cylan.smart.plugin.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.data.bean.VisitorData
import com.cylan.smart.plugin.widget.CircleImageView
import java.text.SimpleDateFormat

/**
 * @author Lupy create on 19-1-14
 * @Description
 */

class VisitorListAdapter(context: Context, var visitors: List<VisitorData>) :
        LoadMoreAdapter<RecyclerView.ViewHolder>(context) {

    val TITLE: Int = 1
    val ITEM: Int = 2
    var dateFormat = SimpleDateFormat("yyy.MM.dd HH:mm")

    var onVisitorItemClickListener: OnVisitorItemClickListener? = null

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TITLE
        }
        return ITEM
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        if (p1 == TITLE) {
            return object :
                    RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.home_goup_visitorlist_title, p0, false)) {}
        } else {
            return ViewHolderHelper(layoutInflater.inflate(R.layout.home_statisticslist_item, p0, false)).apply {
                itemView.setOnClickListener {
                    onVisitorItemClickListener?.onVisitorItemClicked(itemView, adapterPosition)
                }
            }
        }
    }

    interface OnVisitorItemClickListener {
        fun onVisitorItemClicked(itemView: View, position: Int)
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is ViewHolderHelper) {
            var visitor = visitors[p1 - 1]
            var holder = p0 as ViewHolderHelper

            if (visitor.register_url.isNullOrEmpty()) {
                Glide.with(context).load(visitor.face_url)
                        .apply(
                                RequestOptions().placeholder(R.mipmap.icon_head)
                                        .error(R.mipmap.icon_head)
                        ).into(holder.img)
            } else {
                Glide.with(context).load(visitor.register_url).apply(
                        RequestOptions().placeholder(R.mipmap.icon_head)
                                .error(R.mipmap.icon_head)
                ).into(holder.img)
            }

            holder.name.text = visitor.person_name
            holder.count.text = "${visitor.visit_num}"
            holder.visitorTime.text = "最后来访于　${dateFormat.format(visitor.time * 1000)}"
            holder.gender.isEnabled = "female".equals(visitor.gender) ?: false
            holder.gender.text = "${visitor.age}"
//            holder.itemView.setOnClickListener {
//                Log.e("lupengyun", visitor.toString())
//            }
        } else {

        }
    }


    override fun getItemCount(): Int {
        if (visitors.size == 0) {
            return 0
        } else {
            return visitors.size + 1
        }
    }


    inner class ViewHolderHelper(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img: CircleImageView
        var name: TextView
        var gender: TextView
        var visitorTime: TextView
        var count: TextView

        init {
            img = itemView.findViewById(R.id.cimg_face)
            name = itemView.findViewById(R.id.tv_visitor_name)
            gender = itemView.findViewById(R.id.tv_visitor_gender)
            visitorTime = itemView.findViewById(R.id.tv_last_visit_time)
            count = itemView.findViewById(R.id.tv_visitor_count)
        }

    }


}