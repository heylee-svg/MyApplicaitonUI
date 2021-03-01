package com.cylan.smart.plugin.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.cylan.smart.plugin.R

/**
 * @author Lupy
 * @since 19-6-25
 * @desc $
 */


class RecyclerItemDocor(context: Context,var devideWidth:Float = 1f) : RecyclerView.ItemDecoration() {

    var paint: Paint

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.strokeWidth = devideWidth
        paint.style = Paint.Style.FILL
        paint.color = context.resources.getColor(R.color.devide_line)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView) {
        super.onDraw(c, parent)
        for (i in 0..parent.childCount) {
            var childView = parent.getChildAt(i)
            childView?.apply {
                c.drawLine(0f, bottom.toFloat(), parent.width.toFloat(), bottom.toFloat(), paint)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom += devideWidth.toInt()
        super.getItemOffsets(outRect, view, parent, state)
    }
}