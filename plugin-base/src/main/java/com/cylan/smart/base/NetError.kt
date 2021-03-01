package com.cylan.smart.base

import com.cylan.smart.base.constant.ConstantField
import io.reactivex.functions.Consumer
import retrofit2.HttpException

/**
 * @author Lupy create on 19-1-18
 * @Description　网络错误集中处理
 */

class NetError(var listener: (Any?, Int) -> Unit) : Consumer<Throwable> {
    override fun accept(t: Throwable?) {
        if (t is HttpException) {
            if (t.code() == 408) {//请求超时
                listener.invoke(null, ConstantField.LOAD_FAILS)
            } else if(t.code() == 404){
                listener.invoke(null, ConstantField.LOAD_FAILS)
            }else {//其余的均为网络错误
                listener.invoke(null, ConstantField.NET_ERROR)
            }
        } else {//服务起错误
            t?.message?.let {
                logger?.debug(it)
            }
            listener.invoke(null, ConstantField.NET_ERROR)
        }
    }
}