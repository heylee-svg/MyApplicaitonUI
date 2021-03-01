package com.cylan.smart.plugin.ui

/**
 * @author Lupy create on 19-1-14
 * @Description 最基本的视图操作　
 */

interface BaseView {

    /**
     * 开始网络请求
     */
    fun loadBegin()

    /**
     * 网络请求成功
     */
    fun loadSuccess()

    /**
     * 网络请求失败
     */
    fun loadFails()

    /**
     * 网络链接问题
     */
    fun netError()

}