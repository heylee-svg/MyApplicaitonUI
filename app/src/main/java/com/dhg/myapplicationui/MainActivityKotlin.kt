package com.dhg.myapplicationui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.FragmentActivity
//import com.alibaba.android.arouter.facade.annotation.Route

/**
 * @author: Denghg  @createDate: 2021/2/27 上午11:21
 * @description
 **/
//@Route(path = "/home/activityKotlin")
class MainActivityKotlin : FragmentActivity() {
    //    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//        setContentView(R.layout.activity_main3)
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
    }
}