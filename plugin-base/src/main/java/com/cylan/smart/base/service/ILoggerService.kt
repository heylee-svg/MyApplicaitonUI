package com.cylan.smart.base.service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 *
 * @author yanzhendong
 * @since 2019/1/11 下午3:50
 */
@Suppress("unused")
interface ILoggerService : IProvider {

    fun verbose(message: String)

    fun debug(message: String)

    fun info(message: String)

    fun warning(message: String)

    fun error(message: String)

    fun verbose(tag: String, message: String)

    fun debug(tag: String, message: String)

    fun info(tag: String, message: String)

    fun warning(tag: String, message: String)

    fun error(tag: String, message: String)
}