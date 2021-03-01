package com.cylan.smart.base.databinding


import android.view.View
import androidx.databinding.BindingAdapter

/**
 *
 * @author yanzhendong
 * @since 2019/1/14 下午3:54
 */
@Suppress("unused")
object DatabindingComponent {
    @JvmStatic
    @BindingAdapter("bind:gone")
    fun bindVisibleOrGone(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("bind:invisible")
    fun bindVisibleOrInvisible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }
}