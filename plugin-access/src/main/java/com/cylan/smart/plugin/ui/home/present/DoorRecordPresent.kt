package com.cylan.smart.plugin.ui.home.present

import android.content.Context
import com.cylan.smart.base.widget.calendarview.DateUtils
import com.cylan.smart.plugin.adapter.DoorReorcAdapter
import com.cylan.smart.plugin.data.DataManger
import com.cylan.smart.plugin.data.bean.DoorAccess
import com.cylan.smart.plugin.data.request.DoorAccessListRequest
import com.cylan.smart.plugin.ui.BasePresent
import com.cylan.smart.plugin.ui.home.iview.DoorRecordView
import java.time.DateTimeException
import java.util.*
import kotlin.collections.ArrayList

/**
 *@author Lupy Create on 2019-08-14.
 *@description
 */

class DoorRecordPresent(var context: Context?, doorRecordView: DoorRecordView) :
    BasePresent<DoorRecordView>(doorRecordView) {

    var doorAccesses: ArrayList<DoorAccess> = ArrayList()
    var restoreDoorAccesses: ArrayList<DoorAccess> = ArrayList()
    var doorReorcAdapter: DoorReorcAdapter? = null

    var name: String = ""
    var end_time: Long = DateUtils.getCurrentTime(23, 59, 59) //初始化当前时间
    var start_time: Long = DateUtils.getCurrentTime() //初始化当前时间


    override fun loadData() {
        doorReorcAdapter?.loadMoreEnable = true
        if (doorAccesses.size == 0) {
            baseView.loadBegin()
        }
        var request = DoorAccessListRequest(20, doorAccesses.size)
        request.begin_time = start_time
        request.end_time = end_time
        request.name = name
        DataManger.getInstance().getDoorAccessList(request) { list, i ->
            if (i == 200) {
                if (list == null || list.isEmpty()) {
                    doorReorcAdapter?.loadMoreEnable = false
                } else {
                    doorAccesses.addAll(list)
                    doorReorcAdapter?.notifyDataSetChanged()
                }
                if (doorAccesses.isEmpty()) {
                    baseView.emptyData()
                } else {
                    baseView.loadSuccess()
                }
            } else {
                if (doorAccesses.isEmpty()) {
                    baseView.loadFails()
                }
            }

        }

    }

    /**
     * 重新加载数据
     */
    fun reLoad() {
        doorAccesses.clear()
        //todo 重置时间参数
        loadData()
    }

    fun restoreData() {
        name = ""  //恢复数据
        doorAccesses.clear()
        end_time = DateUtils.getCurrentTime(23, 59, 59) //初始化当前时间
        start_time = DateUtils.getCurrentTime() //初始化当前时间
        doorAccesses.addAll(restoreDoorAccesses)
        restoreDoorAccesses.clear()
        doorReorcAdapter?.notifyDataSetChanged()
        baseView.loadSuccess()

    }

    fun saveData() {
        restoreDoorAccesses.clear()
        restoreDoorAccesses.addAll(doorAccesses)
        doorAccesses.clear()
        doorReorcAdapter?.notifyDataSetChanged()
    }

    fun clearSearchData(){
        doorAccesses.clear()
    }
    fun getAdapter(): DoorReorcAdapter {
        doorReorcAdapter = doorReorcAdapter ?: DoorReorcAdapter(doorAccesses, context!!, false)
        doorReorcAdapter?.loadMore {
            loadData()
        }
        return doorReorcAdapter!!
    }

}