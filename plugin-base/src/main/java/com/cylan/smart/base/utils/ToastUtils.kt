package com.cylan.smart.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.cylan.smart.base.R
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

/**
 *
 * @author yanzhendong
 * @since 2019/1/14 上午11:34
 */
@SuppressLint("StaticFieldLeak")
@Suppress("unused")
object ToastUtils {
    private var toast: Toast? = null
    private var tv: TextView? = null

    private fun initToast(ctx: Context) {
        if (toast == null) {
            tv = View.inflate(ctx.applicationContext, R.layout.toast_text, null) as TextView
            toast = Toast(ctx.applicationContext)
            toast!!.view = tv
        }
    }

    fun showToast(cxt: Context, content: String) {
        showToast(cxt, content, Gravity.CENTER, Toast.LENGTH_SHORT)
    }

    fun showToast(cxt: Context, content: String, gravity: Int) {
        showToast(cxt, content, gravity, 2000)
    }

    fun showToast(cxt: Context, content: String, gravity: Int, duration: Int) {
        try {
            initToast(cxt)
            toast!!.setGravity(gravity, 0, 0)
            toast!!.duration = duration
            tv!!.text = content
            tv!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            toast!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun showSuccessToast(cxt: Context, content: String) {
        try {
            initToast(cxt)
            tv!!.text = content
            toast!!.setGravity(Gravity.CENTER, 0, 0)
            toast!!.duration = Toast.LENGTH_SHORT
            tv!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_notify_result, 0, 0)
            toast!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun showFailToast(cxt: Context, content: String) {
        try {
            initToast(cxt)
            toast!!.setGravity(Gravity.CENTER, 0, 0)
            toast!!.duration = Toast.LENGTH_SHORT
            tv!!.text = content
            tv!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_fail_notify_result, 0, 0)
            toast!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    /**
     * 普通的 Toast
     *
     * @param ctx
     * @param content
     */
    fun showOrdinaryToast(ctx: Context, content: String) {
        Toast.makeText(ctx, content, Toast.LENGTH_SHORT).show()
    }

    fun cancelToast() {
        if (toast != null) {
            toast!!.cancel()
        }
    }

}