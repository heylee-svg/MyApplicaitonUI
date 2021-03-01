package com.cylan.smart.plugin.ui.doorrecord

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.adapter.DoorReorcAdapter
import com.cylan.smart.plugin.data.DataManger
import com.cylan.smart.plugin.data.bean.DoorAccess
import com.cylan.smart.plugin.data.bean.Member
import com.cylan.smart.plugin.data.bean.MemberDetail
import com.cylan.smart.plugin.data.request.DoorAccessInfoRequest
import com.cylan.smart.plugin.help.toHiden
import com.cylan.smart.plugin.help.toVisible
import com.cylan.smart.plugin.ui.BaseActivity
import com.cylan.smart.plugin.widget.RecyclerItemDocor
import kotlinx.android.synthetic.main.home_doorrecord_details_activity_layout.*
import java.text.SimpleDateFormat

/**
 * @author Lupy
 * @since 2019/8/17
 * 门禁记录详情页面
 */
@Route(path = "/home/doorrecord")
class DoorRecordDetailsActivity : BaseActivity() {

    var hasReco: Boolean = false
    var reordList = ArrayList<DoorAccess>()
    var limit = 100
    lateinit var doorAccess: DoorAccess
    lateinit var memberInfo :Member
    var yyyyMMddFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val listAdapter: DoorReorcAdapter by lazy {
        DoorReorcAdapter(reordList, this, true)
    }



    override fun layoutRes(): Int = R.layout.home_doorrecord_details_activity_layout

    override fun initView() {

        //是否是识别人员
        hasReco = intent.getBooleanExtra("hasReco", false)
        doorAccess = intent.getParcelableExtra<DoorAccess>("info")
        if (hasReco) {
            titleTv?.text = "人物详情"
            getMemberInfo(doorAccess.person_id) //获取人物详情头部信息
            doorRecordList.layoutManager = LinearLayoutManager(this)
            doorRecordList.addItemDecoration(RecyclerItemDocor(this))
            doorRecordList.adapter = listAdapter
            listAdapter.loadMore {
                loadData()
            }
            loadData()
        } else {
            titleTv?.text = "抓拍详情"
            doorRecordList.toHiden()
            visitorLayout.toVisible()
            Glide.with(this).load(doorAccess.face_url)
                .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                .error(R.mipmap.avatar_placeholder)
                .into(captureImage)
            name.text=if ("".equals(doorAccess.person_name))"访客" else " "
            deviceNameAndTime.text="抓拍机:${doorAccess.camera_name} 抓拍时间:${yyyyMMddFormat.format(doorAccess.time*1000)}"

        }

    }

    override fun useCommonTitle(): Boolean = true


    /**
     * 加载数据
     */
    fun loadData() {
        var request = DoorAccessInfoRequest(doorAccess.person_id!!, limit, reordList.size)
        request.begin_time = 0L
        request.end_time = System.currentTimeMillis() / 1000
        DataManger.getInstance().getDoorAccessInfo(request) { response, status ->
            if (status == 200 && response != null) {
                if (response.datas.size != 0) {
                    reordList.addAll(response.datas)
                    listAdapter.faceGroup =response?.person_info?.face_repository_name
                    listAdapter.notifyDataSetChanged()
                } else {
                    listAdapter.loadMoreEnable = false
                }
            }
        }
    }

    fun getMemberInfo(person_id:String){
        DataManger.getInstance().getMemberDetail(person_id){
            detail,status ->
            if(status ==ConstantField.SUCCESS){
                if(detail?.members?.size!=0){
                    memberInfo=detail!!.members?.get(0)
                    memberInfo.face_url=detail.member_faces[0].face_url
                    listAdapter.member=memberInfo
                    listAdapter.notifyDataSetChanged()
                }
            }

        }

    }
}