package com.cylan.smart.plugin.adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.data.bean.KaoqinInfo
import com.cylan.smart.plugin.ui.doorrecord.CommonListActivity
import com.cylan.smart.plugin.ui.doorrecord.CommonPresentFactory
import kotlinx.android.synthetic.main.home_kaoqin_list_item_layout.view.*

/**
 * @author Lupy
 * @since 2019/8/18
 */
class KaoQinListAdapter(context: Context, var dataList: List<KaoqinInfo>) :
    LoadMoreAdapter<KaoQinListAdapter.KaoqinItemHolder>(context) {
    var month: String = ""

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): KaoqinItemHolder {
        return KaoqinItemHolder(
            layoutInflater.inflate(
                R.layout.home_kaoqin_list_item_layout,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: KaoqinItemHolder, position: Int) {

        holder.itemView.setOnClickListener {
            var intent = Intent(it.context, CommonListActivity::class.java)
            intent.putExtra("TYPE", CommonPresentFactory.PresentType.PERSONALKAOQIN)
            intent.putExtra("PARAMETER_1", month)
            intent.putExtra("PARAMETER_2", dataList[position].person_id)

            it.context.startActivity(intent)

        }
        Glide.with(context)
            .load(dataList[position].face_url)
            .transform(MultiTransformation(CenterCrop(), CircleCrop()))
            .into(holder.itemView.imgSrc)
        holder.name.text = dataList[position].person_name
        holder.title.text = dataList[position].job
        holder.normalNumber.text = dataList[position].normal_number.toString()
        if (0 == dataList[position].abnormal_number) {
            holder.abnormalNumber.setTextColor(context.resources.getColor(R.color.text_FF636363))
        } else {
            holder.abnormalNumber.setTextColor(context.resources.getColor(R.color.text_warn_red))
        }
        holder.abnormalNumber.text = dataList[position].abnormal_number.toString()

        val spannable = SpannableStringBuilder()
        var startIndex = spannable.length
        spannable.append("实际出勤(天): ").setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.text_636363)),
            0,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        startIndex = spannable.length
        spannable.append(dataList[position].really_number.toString()).setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.text_39d6ca)),
            startIndex,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        startIndex = spannable.length
        spannable.append("天").setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.text_636363)),
            startIndex,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        holder.worksDays.text = spannable
    }


    inner class KaoqinItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgSrc: ImageView
        var name: TextView
        var title: TextView
        var worksDays: TextView
        var abnormalNumber: TextView
        var normalNumber: TextView

        init {
            imgSrc = itemView.findViewById(R.id.imgSrc)
            name = itemView.findViewById(R.id.empleeName)
            title = itemView.findViewById(R.id.empleeTitle)
            worksDays = itemView.findViewById(R.id.workDays)
            abnormalNumber = itemView.findViewById(R.id.unNormalNumber)
            normalNumber = itemView.findViewById(R.id.normalNumber)
        }

    }

    fun contactBuilder(parms: String): String {
        val spannable = SpannableStringBuilder()
        var startIndex = spannable.length
        spannable.append("实际出勤(天): ").setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.text_636363)),
            0,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        startIndex = spannable.length
        spannable.append(parms).setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.text_39d6ca)),
            startIndex,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        startIndex = spannable.length
        spannable.append("天").setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.text_636363)),
            startIndex,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannable.toString()
    }
}