package com.cylan.smart.base.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.qmuiteam.qmui.util.QMUIDeviceHelper
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUINotchHelper

/**
 *
 * @author yanzhendong
 * @since 2019/1/12 上午10:03
 */
@Suppress("unused")
object ViewUtils {

    fun getDrawable(context: Context, drawable: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawable)
    }

    fun isLand(config: Configuration): Boolean {
        return config.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun isLand(context: Context): Boolean {
        return isLand(context.resources.configuration)
    }

    fun getActivity(context: Context): Activity? {
        var resolverContext = context
        while (resolverContext is ContextWrapper) {
            if (resolverContext is Activity) {
                return resolverContext
            }
            resolverContext = resolverContext.baseContext
        }
        return null
    }

    private fun supportTranslucent(): Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                // Essential Phone 在 Android 8 之前沉浸式做得不全，系统不从状态栏顶部开始布局却会下发 WindowInsets
                && !(QMUIDeviceHelper.isEssentialPhone() && Build.VERSION.SDK_INT < 26))
    }

    @TargetApi(28)
    private fun handleDisplayCutoutMode(window: Window) {
        val decorView = window.decorView
        if (decorView != null) {
            if (ViewCompat.isAttachedToWindow(decorView)) {
                realHandleDisplayCutoutMode(window, decorView)
            } else {
                decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                    override fun onViewAttachedToWindow(v: View) {
                        v.removeOnAttachStateChangeListener(this)
                        realHandleDisplayCutoutMode(window, v)
                    }

                    override fun onViewDetachedFromWindow(v: View) {

                    }
                })
            }
        }
    }

    @TargetApi(28)
    private fun realHandleDisplayCutoutMode(window: Window, decorView: View) {
        if (decorView.rootWindowInsets != null && decorView.rootWindowInsets.displayCutout != null) {
            val params = window.attributes
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams
                .LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = params
        }
    }

    /**
     * 检测 Android 6.0 是否可以启用 window.setStatusBarColor(Color.TRANSPARENT)。
     */
    fun supportTransclentStatusBar6(): Boolean {
        return !(QMUIDeviceHelper.isZUKZ1() || QMUIDeviceHelper.isZTKC2016())
    }

    fun translucent(activity: Activity) {
        if (!supportTranslucent()) {
            // 版本小于4.4，绝对不考虑沉浸式
            return
        }
        val window = activity.window

        if (QMUINotchHelper.isNotchOfficialSupport()) {
            handleDisplayCutoutMode(window)
        }

        // 小米和魅族4.4 以上版本支持沉浸式
        // 小米 Android 6.0 ，开发版 7.7.13 及以后版本设置黑色字体又需要 clear FLAG_TRANSLUCENT_STATUS, 因此还原为官方模式
        if (QMUIDeviceHelper.isMeizu() || QMUIDeviceHelper.isMIUI() && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportTransclentStatusBar6()) {
                // android 6以后可以改状态栏字体颜色，因此可以自行设置为透明
                // ZUK Z1是个另类，自家应用可以实现字体颜色变色，但没开放接口
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            } else {
                // android 5不能修改状态栏字体颜色，因此直接用FLAG_TRANSLUCENT_STATUS，nexus表现为半透明
                // 魅族和小米的表现如何？
                // update: 部分手机运用FLAG_TRANSLUCENT_STATUS时背景不是半透明而是没有背景了。。。。。
                //                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // 采取setStatusBarColor的方式，部分机型不支持，那就纯黑了，保证状态栏图标可见
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.statusBarColor = 0x40000000
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val attributes = window.attributes
            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            attributes.flags = attributes.flags or flagTranslucentStatus
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //attributes.flags |= flagTranslucentNavigation;
            window.attributes = attributes
        }
    }

    fun fullscreen(activity: Activity, fullscreen: Boolean) {
        val attrs = activity.window.attributes
        if (fullscreen) {
            attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        } else {
            attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        }
        activity.window.attributes = attrs
    }

    fun dp2px(context: Context, dp: Int): Int {
        return QMUIDisplayHelper.dp2px(context, dp)
    }
}