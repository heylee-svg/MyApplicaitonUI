package com.cylan.smart.base.service.impl

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService

/**
 *
 * @author yanzhendong
 * @since 2019/1/11 上午11:58
 */
@Suppress("unused")
@Route(path = "/base/degrade_service", name = "全局降级策略服务")
class DegradeServiceImpl : DegradeService {
    override fun onLost(context: Context?, postcard: Postcard?) {
        Log.i(TAG, "$TAG,$postcard")
//        QMUITipDialog.Builder()
    }

    override fun init(context: Context?) {
        Log.i(TAG, "$TAG.init")
    }

    companion object {
        private const val TAG = "DegradeServiceImpl"
    }
}