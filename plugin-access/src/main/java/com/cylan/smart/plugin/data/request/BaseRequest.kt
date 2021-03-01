package com.cylan.smart.plugin.data.request

import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.base.storage
import com.cylan.smart.plugin.ui.home.globalBrandId
import com.cylan.smart.plugin.ui.home.globalShopId

/**
 *@author Lupy Create on 2019-08-26.
 *@description 请求基类
 *
 * time 请求时间，单位：秒
 * auth_token 登录返回token
 * account 账号
 * brand_id 品牌ID
 * shop_id 店铺ID
 *
 */
open class BaseRequest {
    var time:Long
    var auth_token:String?
    var account:String?
    var brand_id:String?
    var shop_id:String?

    init {
        this.time = System.currentTimeMillis() / 1000
        this.auth_token = storage?.getString(ConstantField.TOKEN, "")
        this.account = storage?.getString(ConstantField.ACCOUNT, "")
        this.shop_id = globalShopId
        this.brand_id = globalBrandId
    }

}