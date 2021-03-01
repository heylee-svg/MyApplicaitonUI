/*
 * Copyright 2018 qiugang(thisisqg@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cylan.smart.base

import android.text.TextUtils
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCore {

    companion object {
        @Volatile
        private var retrofit: Retrofit? = null

        private fun getOne(): Retrofit {
            return retrofit ?: synchronized(
                RetrofitCore::class
            ) {
                retrofit
                    ?: buildRetrofit().also { retrofit = it }
            }
        }

        fun reset() {
            retrofit = null
            getOne()
        }

        private fun buildRetrofit(): Retrofit {
            var hostStr = storage?.getString("HOST", OEM.getInstance().host)
            hostStr = if (TextUtils.isEmpty(hostStr)) OEM.getInstance().host else hostStr
            if (!hostStr!!.startsWith("http") && !hostStr.startsWith("https")) {
                hostStr = "http://$hostStr"
            }
            val baseUrl = "$hostStr:${storage?.getInt("PORT", OEM.getInstance().port)}"
            val okHttpClient = OkHttpProvider.okhttp()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'").create()
                    )
                )
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        fun <T> service(clazz: Class<T>): T {
            return getOne()
                .create(clazz)
        }
    }
}