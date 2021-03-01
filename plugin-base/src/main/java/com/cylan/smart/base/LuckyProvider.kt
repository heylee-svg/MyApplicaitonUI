package com.cylan.smart.base

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 *
 * @author yanzhendong
 * @since 2019/1/12
 */
@Suppress("unused")
class LuckyProvider {
    private val mServiceMap = ConcurrentHashMap<Class<*>, Any>()
    private val mRegisterObservers = CopyOnWriteArrayList<Observer>()
    private var mCleared = false
    fun register(service: Class<*>, serviceImp: Any) {
        if (mCleared) return
        mServiceMap[service] = serviceImp
        for (observer in mRegisterObservers) {
            observer.onAdd(service, serviceImp)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> visit(service: Class<T>): T? {
        return mServiceMap[service] as T?
    }

    fun addObserver(observer: Observer) {
        mRegisterObservers.addIfAbsent(observer)
    }

    fun removeObserver(observer: Observer) {
        mRegisterObservers.remove(observer)
    }

    fun clear() {
        mCleared = true
        mRegisterObservers.forEach { it.onClear() }
        mRegisterObservers.clear()
        mServiceMap.clear()
    }

    interface Observer {
        fun onAdd(service: Class<*>, serviceImp: Any)
        fun onRemove(service: Class<*>, serviceImp: Any)
        fun onClear()
    }
}