package com.cylan.smart.plugin.ui.doorrecord.present

import android.view.View
import androidx.annotation.DrawableRes
import com.cylan.smart.plugin.ui.BaseView

/**
 * @author Lupy
 * @since 2019/8/19
 */

interface CommonBaseView : BaseView {
    /**
     * 初始化title
     */
    fun initPageTilte(title: String)

    /**
     * 初始化右侧文字
     */
    fun initPageRightText(text: String, function: () -> Unit)

    /**
     * 初始化右侧图标
     */
    fun initPageRightIcon(@DrawableRes iconRes: Int, function: () -> Unit)
}