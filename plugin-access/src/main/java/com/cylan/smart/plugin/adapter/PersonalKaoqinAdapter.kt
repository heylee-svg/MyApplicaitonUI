package com.cylan.smart.plugin.adapter

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cylan.smart.base.utils.hhmmFormat
import com.cylan.smart.base.utils.mmddWeekFormat
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.data.bean.KaoqinInfo
import com.cylan.smart.plugin.data.bean.KaoqinPersonInfo
import com.cylan.smart.plugin.data.bean.KaoqinState
import com.cylan.smart.plugin.data.bean.KaoqinTotal
import kotlinx.android.synthetic.main.home_kaoqin_list_item_layout.view.*
import kotlinx.android.synthetic.main.home_personal_kaoqin_head_item.view.*
import kotlinx.android.synthetic.main.home_personal_kaoqin_item.view.*
import retrofit2.http.HEAD
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

/**
 * @author Lupy
 * @since 2019/8/20
 */
class PersonalKaoqinAdapter(
    var context: Context,
    var personInfo: KaoqinInfo?,
    var list: List<KaoqinState>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val head: Int = 1
    private val item: Int = 2

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return head
        }
        return item
    }

    override fun onCreateViewHolder(group: ViewGroup, type: Int):
            RecyclerView.ViewHolder = when (type) {
        head -> HeaderViewHolder(
            layoutInflater.inflate(
                R.layout.home_personal_kaoqin_head_item,
                group,
                false
            )
        )
        else -> PersonalViewHolder(
            layoutInflater.inflate(
                R.layout.home_personal_kaoqin_item,
                group,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        if (list.size == 0) {
            return 1
        } else {
            return list.size + 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p: Int) {


        if (holder is HeaderViewHolder) {
            Glide.with(context).load(personInfo?.face_url)
                .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                .into(holder.headImg)

            holder.name.text = "姓名：${personInfo?.person_name}"
            holder.shouldNumber.text = "应出勤(天):${personInfo?.should_number.toString()}"
            holder.actualNumber.text = "实际出勤(天):${personInfo?.really_number.toString()}"
            if ("male".equals(personInfo?.gender)) {
                holder.gender.text = "性别:男"
            } else if ("female".equals(personInfo?.gender)) {
                holder.gender.text = "性别:女"
            } else {
                holder.gender.text = "性别:"
            }
            holder.job.text = "职位:${personInfo?.job}"
            holder.faceGroup.text = "脸库:${personInfo?.group}"
            holder.normalTotal.text = personInfo?.normal_number.toString()
            if (0 == personInfo?.abnormal_number) {
                holder.abnormalTotal.setTextColor(context.resources.getColor(R.color.text_FF636363))
            } else {
                holder.abnormalTotal.setTextColor(context.resources.getColor(R.color.text_warn_red))
            }
            holder.abnormalTotal.text = personInfo?.abnormal_number.toString()
        } else {
            holder as PersonalViewHolder

            holder.date.text = mmddWeekFormat(list[p - 1].time)
            var startWork =
                if (0L == list[p - 1].start_work_time.absoluteValue) "" else hhmmFormat(list[p - 1].start_work_time)

            var endWork =
                if (0L == list[p - 1].end_work_time.absoluteValue) "" else hhmmFormat(list[p - 1].end_work_time)

                holder.workTime.text = "上班:${startWork} - 下班:${endWork}"


            when (list[p - 1].status) {
                0 ->
                    holder.state.visibility = View.GONE

                1 -> {
                    holder.state.visibility = View.VISIBLE
                    holder.state.text = "迟到"
                }

                2 -> {
                    holder.state.visibility = View.VISIBLE
                    holder.state.text = "早退"
                }

                3 -> {
                    holder.state.visibility = View.VISIBLE
                    holder.state.text = "迟到、早退"
                }

                4 -> {
                    holder.state.visibility = View.VISIBLE
                    holder.state.text = "缺勤"
                }

                8 -> {
                    holder.state.visibility = View.VISIBLE
                    holder.state.text = "缺卡"
                }

                else ->
                    holder.state.visibility = View.GONE
            }

        }
    }


    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var headImg: ImageView
        var name: TextView
        var shouldNumber: TextView
        var actualNumber: TextView
        var gender: TextView
        var job: TextView
        var faceGroup: TextView
        var normalTotal: TextView
        var abnormalTotal: TextView

        init {
            headImg = itemView.findViewById(R.id.headImg)
            name = itemView.findViewById(R.id.name)
            shouldNumber = itemView.findViewById(R.id.shouldNumber)
            actualNumber = itemView.findViewById(R.id.actualNumber)
            gender = itemView.findViewById(R.id.gender)
            job = itemView.findViewById(R.id.job)
            faceGroup = itemView.findViewById(R.id.face_group)
            normalTotal = itemView.findViewById(R.id.normalNumber)
            abnormalTotal = itemView.findViewById(R.id.abnormalNumber)
        }
    }


    inner class PersonalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView
        var workTime: TextView
        var state: TextView

        init {
            date = itemView.findViewById(R.id.date)
            workTime = itemView.findViewById(R.id.workTime)
            state = itemView.findViewById(R.id.options)
        }
    }

}