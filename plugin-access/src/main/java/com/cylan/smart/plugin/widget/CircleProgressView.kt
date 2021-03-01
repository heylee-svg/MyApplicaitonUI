package com.cylan.smart.plugin.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar
import com.cylan.smart.plugin.R

/**
 * @author Lupy
 * @since 2019/8/18
 */
class CircleProgressView(context: Context, attributeSet: AttributeSet) : ProgressBar(context, attributeSet) {

    var progressPaint: Paint
    var darkPaint: Paint
    var progTextPaint: Paint
    var totalTextPaint: Paint

    lateinit var rect: RectF
    var fontH: Float = 0f
    var strokeW = 20f
    var prog: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var maxPro: Int = 100
        set(value) {
            field = value
            totalTextPaint.measureText("$maxPro")
            totalTextPaint.fontMetrics.apply {
                fontH = descent - ascent
            }
        }

    init {
        progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        progressPaint.strokeWidth = strokeW
        progressPaint.style = Paint.Style.STROKE
        progressPaint.color = resources.getColor(R.color.text_3CDEC2)

        progTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        progTextPaint.style = Paint.Style.FILL
        progTextPaint.color = resources.getColor(R.color.text_3CDEC2)
        progTextPaint.textAlign = Paint.Align.CENTER
        progTextPaint.textSize = 80f

        totalTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        totalTextPaint.style = Paint.Style.FILL
        totalTextPaint.color = resources.getColor(R.color.text_999999)
        totalTextPaint.textAlign = Paint.Align.CENTER
        totalTextPaint.textSize = 40f

        darkPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        darkPaint.strokeWidth = strokeW
        darkPaint.style = Paint.Style.STROKE
        darkPaint.color = resources.getColor(R.color.text_999999)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect = RectF(strokeW / 2, strokeW / 2, w.toFloat() - strokeW / 2, h.toFloat() - strokeW / 2)
    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.drawArc(rect, -90f, 360f, false, darkPaint)
        canvas?.drawArc(rect, 90f, prog.toFloat() / maxPro.toFloat() * 360, false, progressPaint)
        canvas?.drawText("$prog", width.toFloat() / 2, height.toFloat() / 2, progTextPaint)
        canvas?.drawText("/$maxPro", width.toFloat() / 2, height.toFloat() / 2 + fontH, totalTextPaint)
    }

}