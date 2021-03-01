package com.cylan.smart.plugin.data.bean

/**
 * @author Lupy create on 19-1-21
 * @Description 统计数据
 */

data class StaticsData(
    var begin_time: Long,
    var sub_time: String,
    var total: Int,
    var vip_num: Int,
    var gender_stats: GenderStatus,
    var age_stats: List<AgeStatus>
) {
    inner class GenderStatus(male: Int, female: Int) {
        var male: Int = male
        var female: Int = female
    }

    inner class AgeStatus(age: String, num: Int) {
        var age: String = age
        var num: Int = num
    }

}