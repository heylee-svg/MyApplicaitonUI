package com.cylan.smart.base.utils

import java.text.SimpleDateFormat

/**
 *@author Lupy Create on 2019-09-01.
 *@description 时间格式化工具类
 */

var yyyyMMddFormat:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
var mmddWeekFormat:SimpleDateFormat = SimpleDateFormat("MM月dd日(E)")

var HHmmFormat = SimpleDateFormat("HH:mm")  //使用了默认的格式创建了一个日期格式化对象。

/**
 * 格式化时间为  @sample yyyy-MM-dd
 */
fun yMdFormat(time:Long):String{
    return yyyyMMddFormat.format(time*1000)
}
fun mmddWeekFormat(time :Long):String{
    return mmddWeekFormat.format(time*1000)
}
fun hhmmFormat(time :Long ):String{
    return HHmmFormat.format(time*1000)
}