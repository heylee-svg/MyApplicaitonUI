package com.cylan.smart.plugin.ui.home.fragment.kaoqin

import androidx.fragment.app.Fragment
import com.cylan.smart.plugin.ui.home.fragment.base.TablayoutBaseFragment

/**
 *@author Lupy Create on 2019-08-12.
 *@description
 */

class KaoqinRecordFragment : TablayoutBaseFragment() {

    val dayKaoqinFragment:DaoKaoqinFragment by lazy {
        DaoKaoqinFragment()
    }

    val monthKaoqinFragment:MonthKaoqinFragment by lazy {
        MonthKaoqinFragment()
    }

    override fun onTitles(): Array<String> {
        return arrayOf("当天考勤", "当月考勤")
    }

    override fun loadChildFragmentByIndex(index: Int): Fragment {
        return when (index) {
            0 -> dayKaoqinFragment
            1 -> monthKaoqinFragment
            else -> monthKaoqinFragment//默认也返回月的吧
        }
    }


    override fun OnShopIdOrBrandIdChange() {
        dayKaoqinFragment.OnShopIdOrBrandIdChange()
        monthKaoqinFragment.OnShopIdOrBrandIdChange()
    }

}