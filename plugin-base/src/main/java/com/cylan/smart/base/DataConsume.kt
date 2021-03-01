package com.cylan.smart.base

import io.reactivex.functions.Consumer

/**
 * @author Lupy create on 19-1-18
 * @Description　对请求结果的集中处理
 */

class DataConsume<T>(var listener: (T) -> Boolean) : Consumer<T> {
    override fun accept(t: T) {

        if (listener.invoke(t)) {
            return
        }

        if (t is BaseResponse) {
            handlerRescode(t.code ?: 0)
        }
    }

    /**
     * 处理返回码
     */
    fun handlerRescode(code: Int) = when (code) {
        401 -> {
            BroadcastManager.instance.sendAuthTokeninvalidate()
        }
        else -> {

        }
    }

}