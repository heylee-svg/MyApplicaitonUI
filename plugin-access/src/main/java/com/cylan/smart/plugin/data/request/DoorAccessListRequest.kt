package com.cylan.smart.plugin.data.request

/**
 *@author Lupy Create on 2019-08-27.
 *@description 门禁记录列表请求
 */

class DoorAccessListRequest(var limit:Int,var offset:Int) : BaseRequest(){
    var name:String? = null
    var tel:String ?=null
    var begin_time:Long?= null
    var end_time:Long? = null


}