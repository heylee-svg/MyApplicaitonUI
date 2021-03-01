package com.cylan.smart.plugin.ui.doorrecord

import android.app.Activity
import android.view.MotionEvent
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.ui.BaseActivity

@Route(path = "/home/showPic")
class ShowPicActivity : BaseActivity() {
    override fun layoutRes() = R.layout.home_door_record_photo

    override fun initView() {
        var face_url = intent.getStringExtra("face_url")

        var largeImage = findViewById<ImageView>(R.id.large_img)
        Glide.with(this).load(face_url)
            .into(largeImage)

    }

    override fun useCommonTitle() = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        this.finish()
        this.overridePendingTransition(0, R.anim.zoom_exit)
        return super.onTouchEvent(event)
    }
}