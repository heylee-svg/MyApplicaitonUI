package com.cylan.smart.base.service.impl

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.cylan.smart.base.service.IStorageService
import com.tencent.mmkv.MMKV

/**
 *
 * @author yanzhendong
 * @since 2019/1/9 下午8:23
 */
@Suppress("unused")
@Route(path = "/base/mmkv_storage_service", name = "键值属性存储服务")
class MMKVStorageServiceImpl : IStorageService {
    override fun remove(key: String) {
        MMKV.defaultMMKV().removeValueForKey(key)
    }

    override fun putInt(key: String, value: Int) {
        MMKV.defaultMMKV().encode(key, value)
    }

    override fun putString(key: String, value: String?) {
        MMKV.defaultMMKV().encode(key, value)
    }

    override fun putBoolean(key: String, value: Boolean) {
        MMKV.defaultMMKV().encode(key, value)
    }

    override fun putLong(key: String, value: Long) {
        MMKV.defaultMMKV().encode(key, value)
    }

    override fun putFloat(key: String, value: Float) {
        MMKV.defaultMMKV().encode(key, value)
    }

    override fun putStringSet(key: String, value: Set<String>?) {
        MMKV.defaultMMKV().encode(key, value)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return MMKV.defaultMMKV().decodeInt(key, defaultValue)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return MMKV.defaultMMKV().decodeString(key, defaultValue)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return MMKV.defaultMMKV().decodeBool(key, defaultValue)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return MMKV.defaultMMKV().decodeLong(key, defaultValue)
    }

    override fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? {
        return MMKV.defaultMMKV().decodeStringSet(key, defaultValue)
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return MMKV.defaultMMKV().decodeFloat(key, defaultValue)
    }

    override fun clearAll() {
        MMKV.defaultMMKV().clearAll()
    }

    override fun init(context: Context?) {
        MMKV.initialize(context)
    }
}