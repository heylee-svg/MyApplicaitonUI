package com.cylan.smart.plugin.ui.home

import org.junit.Test

/**
 * @author Lupy create on 19-2-25
 * @Description
 */

class Test {

    @Test
    fun test() {
        println("-1 status is ${getNetStatus(-1)}")
        println("-2 status is ${getNetStatus(-2)}")
        println("1 status is ${getNetStatus(1)}")
        println("4 status is ${getNetStatus(4)}")
        println("10 status is ${getNetStatus(10)}")
        println("100 status is ${getNetStatus(100)}")
        println("0 status is ${getNetStatus(0)}")
    }


    fun getNetStatus(status: Int) = when (status) {
        -1, -2 -> "离线"
        1 -> "WIFI连接"
        2 -> "2G连接"
        3 -> "3G连接"
        4 -> "4G连接"
        5 -> "5G连接"
        10 -> "有线连接"
        else -> "离线"
    }
}