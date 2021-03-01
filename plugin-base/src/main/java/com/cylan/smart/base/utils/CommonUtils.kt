package com.cylan.smart.base.utils

/**
 * @author Lupy create on 19-1-17
 * @Description　公众的一些方法
 */
object CommonUtils {

    /**
     * 验证手机号
     */
    fun isPhoneNumber(phone: String?): Boolean {
        if (phone.isNullOrEmpty() || phone.isNullOrBlank()) {
            return false
        }

        var regex = """^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\d{8}"""
        return regex.toRegex().matches(phone)
    }
}