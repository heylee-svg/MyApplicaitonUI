package com.cylan.smart.plugin.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.graphics.DashPathEffect
import com.cylan.smart.plugin.R


/**
 * @author Lupy
 * @since 2019/8/18
 */
class DotView(context: Context, attributes: AttributeSet) : View(context,attributes) {

    var dotPaint:Paint

    init {
        dotPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dotPaint.style = Paint.Style.STROKE
        dotPaint.color = resources.getColor(R.color.devide_line)
        dotPaint.strokeWidth = 2f
        dotPaint.pathEffect = DashPathEffect(floatArrayOf(3f, 4f), 0f)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawLine((width / 2).toFloat(),0f, (width / 2).toFloat(), height.toFloat(),dotPaint)
    }
}