package com.cylan.smart.base.utils


import android.app.Activity
import android.content.Context


import android.view.inputmethod.InputMethodManager

fun showInputKeyboard(activity: Activity) {
    val imm =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_FORCED)
}

fun hideInputKeyboard(activity: Activity) {
    val manager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (activity.getCurrentFocus() != null && activity!!.getCurrentFocus()?.getWindowToken() != null) {
        manager.hideSoftInputFromWindow(
            activity!!.getCurrentFocus()?.getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }



}
