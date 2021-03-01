package com.cylan.smart.plugin.data.request

/**
 *@author Lupy Create on 2019-09-01.
 *@description 已识别人员记录详情请求
 */
class DoorAccessInfoRequest(var person_id:String,var limit:Int,var offset:Int) :BaseRequest() {
    var begin_time:Long? = null
    var end_time:Long?= null
}