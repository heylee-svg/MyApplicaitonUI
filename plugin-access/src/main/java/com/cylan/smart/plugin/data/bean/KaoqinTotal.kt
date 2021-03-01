package com.cylan.smart.plugin.data.bean

import com.cylan.smart.base.BaseResponse


/**
 *  考勤状态
 */
data class KaoqinTotal(
    var really_number: Int,//实到
    var should_number: Int,//应到
    var normal_number: Int,//正常考勤
    var abnormal_number: Int//异常考勤
) : BaseResponse()

/**
 * 月考勤人信息
 */

data class KaoqinPersonData(
    var state: KaoqinTotal,
    var person_info: KaoqinPersonInfo,
    var datas: List<KaoqinState>
) : BaseResponse()

data class DayKaoqinInfo(
    var came_late_datas: List<KaoqinInfo> = arrayListOf(),
    var left_early_datas: List<KaoqinInfo> = arrayListOf()
) : BaseResponse()

data class KaoqinPersonInfo(

    var person_name: String = "",//人物姓名
    var gender: String = "",// 性别：male/female
    var job: String = "",//职位
    var face_url: String = "",//脸图地址
    var group: String = ""//脸库
)

data class KaoqinState(
    var time: Long,  //考勤时间
    var start_work_time: Long, //上班时间
    var end_work_time: Long, //下班时间
    var status: Int//考勤状态
)


data class KaoqinInfo(
    var time: Long,
    var person_id: String = "",  //人物标识
    var person_name: String = "",//人物姓名
    var gender: String = "",// 性别：male/female
    var job: String = "",//职位
    var face_url: String = "",//脸图地址
    var group: String = "",//脸库
    var start_work_time: Long, //上班时间
    var end_work_time: Long, //下班时间
    var status: Int,//考勤状态
    var really_number: Int,//实到
    var should_number: Int,//应到
    var normal_number: Int,//正常考勤
    var abnormal_number: Int//


)
