package com.cylan.smart.base

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * @author Lupy
 * @since  2019/8/25
 * @description okhttp
 *
 * <pre>
 *     @code
 * HttpLoggingInterceptor.Level.NONE
 *      不打印log
 *
 *HttpLoggingInterceptor.Level.BASIC
 *      打印请求行 和响应行
 *
 *HttpLoggingInterceptor.Level.HEADERS
 *      打印请求头和状态行
 *
 *HttpLoggingInterceptor.Level.BODY
 *      打印请求行 请求头 请求体
 *          状态行 响应头 响应体
 * </pre>
 *
 */
@Suppress("unused")
object OkHttpProvider {
    private var mOkhttpClient: OkHttpClient? = null
    fun okhttp(): OkHttpClient {
        return mOkhttpClient ?: synchronized(this) {
            if (mOkhttpClient == null) {
                val builder = OkHttpClient.Builder()
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(loggingInterceptor)
                builder.connectTimeout(30.toLong(), TimeUnit.SECONDS)
                builder.readTimeout(20.toLong(), TimeUnit.SECONDS)
                mOkhttpClient = builder.build()
            }
            return@synchronized mOkhttpClient!!
        }
    }
}