package com.cylan.smart.base.constant

/**
 * @author Lupy create on 19-1-17
 * @Description  使用的一些全局的常量
 */


object ConstantField {

    const val ACCOUNT: String = "account"//账号
    const val PWD: String = "password" //密码
    const val TOKEN: String = "auth_token"//token
    const val ACCOUNT_TYPE: String = "account_type"
    const val USER_NAME: String = "user_name"
    const val USER_HEAD: String = "user_head"
    const val PREVIEW_PICTURE = "preview_picture"
    const val BRANDS_SELECT = "brands_select"
    const val BRAND_ID="brand_id"
    const val SHOP_ID="shop_id"
    const val SHOP_NAME="shop_name"


    const val SUCCESS = 200
    const val PASSWORD_ERROR = 3001//密码错误
    const val ACCOUNT_NOT_EXITS = 1004//账号不存在
    const val ACCOUNT_DISABLE = 3105//账号已停用

    const val LOAD_FAILS = 408 //加载错误
    const val NET_ERROR = 556 //网络错误

    enum class AppProfession(val value: Int) {
        /**
         * Smart 新零售
         */
        SMART(3),
        /**
         * 门禁考勤
         */
        DOOR_GUARD(2),
        /**
         * 名厨亮灶
         */
        KITCHEN_GUARD(1);
    }

}