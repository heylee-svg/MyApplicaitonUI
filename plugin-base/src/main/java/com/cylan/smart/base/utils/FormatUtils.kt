package com.cylan.smart.base.utils



/**
 *
 * @author yanzhendong
 * @since 2019/1/18 下午5:26
 */
@Suppress("unused")
object FormatUtils {
    private var TAG = "FormatUtils"

    fun byteToInt(str: String): Int {
        val a = Integer.valueOf(str ,2).toString()
        System.out.println(TAG + "builder is" + " " + a)
        return Integer.valueOf(a)
    }

    /**
     * 默认这里选择的是7位的stringbuilder
     */
    fun intToByte(i: Int): String {
        var a = Integer.toBinaryString(i)
        var builder = StringBuilder()
        for (i in 1..7 - a.length) {
            builder.append("0")
        }
        builder.append(a)
        System.out.println(TAG + "builder is" + " " + builder)
        return builder.toString()
    }

}