package com.cylan.smart.plugin.ui.home.struct

import android.content.Context
import android.widget.RadioButton
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.ui.home.fragment.accesscontrol.DoorRecordFragment
import com.cylan.smart.plugin.ui.home.fragment.base.BaseFragment
import com.cylan.smart.plugin.ui.home.fragment.kaoqin.KaoqinRecordFragment

/**
 *@author Lupy Create on 2019-08-12.
 *@description 门禁考勤首页结构
 */


class DoorGuardStruct : MainBaseStruct(){

    var doorRecordFragment: DoorRecordFragment? = null
    var kaoqinRecordFragment: KaoqinRecordFragment? = null

    override fun initMiddleLRadioButton(context: Context): RadioButton {
        return generateRadioButton(context,"门禁记录", R.drawable.doorguard_btn_drawable_sel)
    }

    override fun initMiddleRRadioButton(context: Context): RadioButton {
        return generateRadioButton(context,"考勤记录",R.drawable.kaoqin_btn_drawable_sel)
    }

    override fun dispatchRefreshContent() {
        doorRecordFragment?.OnShopIdOrBrandIdChange()
    }

    override fun getMiddleLeftFragment(): BaseFragment {
        if(doorRecordFragment == null){
            doorRecordFragment = DoorRecordFragment()
        }
        return  doorRecordFragment!!
    }

    override fun getMiddleRightFragment(): BaseFragment {
        if(kaoqinRecordFragment == null){
            kaoqinRecordFragment = KaoqinRecordFragment()
        }
        return kaoqinRecordFragment!!
    }


}