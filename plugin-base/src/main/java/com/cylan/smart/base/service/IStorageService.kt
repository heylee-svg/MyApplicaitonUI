package com.cylan.smart.base.service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * 存储服务管理
 *
 * @author yanzhendong
 * @since 2019/1/9 下午8:22
 */
@Suppress("unused")
interface IStorageService : IProvider {
    fun putInt(key: String, value: Int)

    fun putString(key: String, value: String?)

    fun putBoolean(key: String, value: Boolean)

    fun putLong(key: String, value: Long)

    fun putFloat(key: String, value: Float)

    fun putStringSet(key: String, value: Set<String>?)

    fun getInt(key: String, defaultValue: Int): Int

    fun getString(key: String, defaultValue: String?): String?

    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun getLong(key: String, defaultValue: Long): Long

    fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>?

    fun getFloat(key: String, defaultValue: Float): Float

    fun remove(key: String)

    fun clearAll()

}