package com.cylan.smart.base.service.impl

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * @author yanzhendong
 * @since 2019/1/9 下午6:06
 */
@Suppress("unused")
@Route(path = "/base/json", name = "对象序列化服务")
class JsonServiceImpl : SerializationService {
    private val mGson = Gson()
    override fun <T : Any?> json2Object(
        input: String?,
        clazz: Class<T>?
    ): T {
        return mGson.fromJson(input, clazz)
    }

    override fun init(context: Context?) {

    }

    override fun object2Json(instance: Any?): String {
        return mGson.toJson(instance)
    }

    override fun <T : Any?> parseObject(
        input: String?,
        clazz: Type?
    ): T {
        return mGson.fromJson(input, clazz)
    }
}