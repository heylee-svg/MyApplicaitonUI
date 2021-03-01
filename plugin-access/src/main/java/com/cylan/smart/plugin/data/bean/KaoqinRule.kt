package com.cylan.smart.plugin.data.bean

/**
 * @author Lupy
 * @since 2019/8/18
 */

data class KaoqinRule(
    var code: Int,
    var msg: String ,
    var request_id :String,
    var work_time_am: String,
    var work_time_pm: String,
    var work_week: Int,
    var enable: Boolean
)



