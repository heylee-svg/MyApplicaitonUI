package com.cylan.smart.plugin.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.base.storage
import com.cylan.smart.base.utils.yMdFormat
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.data.bean.DoorAccess
import com.cylan.smart.plugin.data.bean.Member
import com.cylan.smart.plugin.ui.doorrecord.CommonListActivity
import com.cylan.smart.plugin.ui.doorrecord.CommonPresentFactory
import com.cylan.smart.plugin.ui.doorrecord.ShowPicActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 *@author Lupy Create on 2019-08-14.
 *@description
 */
class DoorReorcAdapter(
    var reordList: List<DoorAccess>,
    context: Context,
    var headInfo: Boolean = true
) :
    LoadMoreAdapter<RecyclerView.ViewHolder>(context) {


    var HEAD_TYPE: Int = 1
    var ITEM_TYPE: Int = 2
    var member: Member? = null
    var faceGroup :String ?=null

    override fun getItemViewType(position: Int): Int {
        if (position == 0 && headInfo) {
            return HEAD_TYPE
        } else {
            return ITEM_TYPE
        }
    }

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEAD_TYPE) {
            return HeadInfoHolder(
                layoutInflater.inflate(
                    R.layout.home_doorrecord_head_item,
                    group,
                    false
                )
            )
        } else {
            return RecordHolder(
                layoutInflater.inflate(
                    R.layout.home_doorrecord_list_item,
                    group,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return if (headInfo) reordList.size + 1 else reordList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecordHolder) {
            var doorAccess: DoorAccess
            if (reordList.size > 0) {
                if (!headInfo) {
                    doorAccess = reordList[position]
                    holder.itemView.setOnClickListener { v ->
                        //todo 跳转详情页面
                        ARouter.getInstance()
                            .build("/home/doorrecord")
                            .withBoolean("hasReco", doorAccess.person_type != 0)
                            .withParcelable("info", doorAccess)
                            .navigation(v.context)
                    }
                    holder.captureImg.setOnClickListener{v ->
                        var intent = Intent(context, ShowPicActivity::class.java)
                        intent.putExtra("face_url", doorAccess.face_url)
                        context.startActivity(intent)
                        (context as Activity).overridePendingTransition(R.anim.zoom_enter, 0)
                    }
                } else {
                    doorAccess = reordList[position - 1]
                }

                Glide.with(context).load(doorAccess.face_url)
                    .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                    .error(R.mipmap.avatar_placeholder)
                    .into(holder.captureImg)
                holder.captureName.text = if(doorAccess.person_name!!.isEmpty()) "访客" else doorAccess.person_name
                holder.captureDeviceAndTime.text =
                    "${doorAccess.camera_name} 抓拍时间:${yMdFormat(doorAccess.time)}"

            }


        } else if (holder is HeadInfoHolder) {

            Glide.with(context).load(member?.face_url)
                .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                .into(holder.image)
            holder.name.text = "姓名:${member?.member_name}"
            holder.tel.text = "手机号码:${member?.mobile}"
            holder.shopName.text = "单位:${storage?.getString(ConstantField.SHOP_NAME, "")}"
            holder.personId.text = "员工工号:${member?.member_card}"
            holder.face_repository.text = "人脸库:${faceGroup}"


        }
    }


    inner class HeadInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var tel: TextView
        var shopName: TextView
        var personId: TextView
        var face_repository: TextView
        var image: ImageView

        init {
            image = itemView.findViewById(R.id.captureImg)
            name = itemView.findViewById(R.id.nameTv)
            tel = itemView.findViewById(R.id.phoneTv)
            shopName = itemView.findViewById(R.id.companyTv)
            personId = itemView.findViewById(R.id.empleeNumTv)
            face_repository = itemView.findViewById(R.id.faceBaseTv)
        }

    }

    inner class RecordHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var captureImg: ImageView
        var captureName: TextView
        var captureDeviceAndTime: TextView

        init {
            captureImg = itemView.findViewById(R.id.doorRecordImg)
            captureName = itemView.findViewById(R.id.recordName)
            captureDeviceAndTime = itemView.findViewById(R.id.captureTime)
        }
    }

}