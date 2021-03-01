package com.cylan.smart.plugin.ui.home.struct

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup
import com.cylan.smart.plugin.R


/**
 *@author Lupy Create on 2019-08-08.
 *@description
 */

abstract class MainBaseStruct : BaseStruct {




    override fun initNavigationBar(group: RadioGroup) {
        //添加主页button
        var homeBtn = initHomeRadioButton(group.context);
        homeBtn.id = R.id.home_navigation_home
        group.addView(homeBtn, generateRadioButtonLayoutParam())
        //添加中左btton
        val middleLRadioButton = initMiddleLRadioButton(group.context)
        middleLRadioButton.id = R.id.home_navigation_middlel
        group.addView(middleLRadioButton, generateRadioButtonLayoutParam())
        //添加中右button
        val middleRRadioButton = initMiddleRRadioButton(group.context)
        middleRRadioButton.id = R.id.home_navigation_middler
        group.addView(middleRRadioButton, generateRadioButtonLayoutParam())
        //添加我的button
        val initMineRadioButton = initMineRadioButton(group.context)
        initMineRadioButton.id = R.id.home_navigation_mine
        group.addView(initMineRadioButton, generateRadioButtonLayoutParam())
    }

    protected fun initHomeRadioButton(context: Context): RadioButton {
        return generateRadioButton(context,"首页",R.drawable.home_btn_drawable_sel)
    }


    abstract fun initMiddleLRadioButton(context: Context): RadioButton

    abstract fun initMiddleRRadioButton(context: Context): RadioButton

    protected fun initMineRadioButton(context: Context): RadioButton {
        return generateRadioButton(context,"我的",R.drawable.me_btn_drawable_sel)
    }


    /**
     * 生成button
     */
    protected fun generateRadioButton(context: Context, btnText: String, drawableRes: Int): RadioButton {
        var radioBtn = RadioButton(context);
        radioBtn.setText(btnText)
        var drawable = context.resources.getDrawable(
            drawableRes
        )
        radioBtn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
        radioBtn.setButtonDrawable(null)
        radioBtn.setTextColor(context.resources.getColorStateList(R.color.home_btn_text_color))
        radioBtn.background = null
        radioBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        radioBtn.gravity = Gravity.CENTER
        radioBtn.setPadding(0, 7, 0, 5)
        return radioBtn
    }

    /**
     * 生成button参数
     */
    private fun generateRadioButtonLayoutParam(): RadioGroup.LayoutParams {
        var layoutParams = RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1f
        layoutParams.gravity = Gravity.CENTER
        return layoutParams
    }



    override fun refreshContent() {

    }

    abstract fun dispatchRefreshContent()
}