package com.cylan.smart.plugin.ui.doorrecord

import com.cylan.smart.plugin.ui.doorrecord.present.CommonBasePresent
import com.cylan.smart.plugin.ui.doorrecord.present.CommonBaseView
import com.cylan.smart.plugin.ui.doorrecord.present.KaoQinListPresent
import com.cylan.smart.plugin.ui.doorrecord.present.PersonalKaoQinPresent

/**
 * @author Lupy
 * @since 2019/8/19
 * 中间件 工厂
 */
object CommonPresentFactory {


    fun getPresentByType(baseView: CommonBaseView,type: PresentType): CommonBasePresent? = when (type) {
        CommonPresentFactory.PresentType.KAOQINLIST -> KaoQinListPresent(baseView)
        CommonPresentFactory.PresentType.PERSONALKAOQIN->PersonalKaoQinPresent(baseView)
        else -> null
    }


    enum class PresentType {
        //单位考勤列表
        KAOQINLIST,
        //个人考勤详情
        PERSONALKAOQIN
    }

}