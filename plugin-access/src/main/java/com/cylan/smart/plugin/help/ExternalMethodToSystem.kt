package com.cylan.smart.plugin.help

import android.app.Activity
import android.view.View

/**
 * @author Lupy create on 19-1-12
 * @Description
 */

/**
 * 置为可见
 */
inline fun View.toVisible() {
    this.visibility = View.VISIBLE
}

/**
 * 只是不可见，还占据空间
 */
inline fun View.toHiden() {
    this.visibility = View.INVISIBLE
}

/**
 * 从空间中移除
 */
inline fun View.removeSpace() {
    this.visibility = View.GONE
}

inline fun Activity.getActivity(): Activity {
    return this
}
