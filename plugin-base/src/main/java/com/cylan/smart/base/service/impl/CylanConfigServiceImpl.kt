package com.cylan.smart.base.service.impl

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.cylan.smart.base.service.IConfigService

/**
 *
 * @author yanzhendong
 * @since 2019/1/11
 */
@Suppress("unused")
@Route(path = "/base/config_service", name = "程序参数配置服务")
class CylanConfigServiceImpl : IConfigService {

    override fun init(context: Context) {

    }
}