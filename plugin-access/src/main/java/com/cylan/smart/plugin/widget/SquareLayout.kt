package com.cylan.smart.plugin.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * @author Lupy
 * @since 2019/8/18
 */

class SquareLayout(context: Context, attributeSet: AttributeSet) : RelativeLayout(context, attributeSet) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width,width)
    }


}