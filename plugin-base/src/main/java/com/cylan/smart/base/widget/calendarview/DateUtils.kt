package com.cylan.smart.base.widget.calendarview

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getCurrentTime(time: Int = 0, minus: Int = 0, mills: Int = 0): Long {
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        var month = Calendar.getInstance().get(Calendar.MONTH) + 1
        var year = Calendar.getInstance().get(Calendar.YEAR)
        var dateString = "${year}-${month}-${day} ${time}:${minus}:${mills}"
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var timeFormat = sdf.parse(dateString).time / 1000
        return timeFormat
    }

    fun getTimeByDate(
        year: Int,
        month: Int,
        day: Int,
        time: Int = 0,
        minus: Int = 0,
        mills: Int = 0
    ): Long {

        var dateString = "${year}-${month}-${day} ${time}:${minus}:${mills}"
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var timeFormat = sdf.parse(dateString).time / 1000
        return timeFormat
    }
}