package com.cylan.smart.plugin.ui

import com.cylan.smart.plugin.data.DataManger

/**
 * @author Lupy create on 19-1-14
 * @Description
 */

abstract class BasePresent<VIEW : BaseView>(var baseView: VIEW) {

    protected var dataManger: DataManger

    init {
        dataManger = DataManger.getInstance()
    }

    abstract fun loadData()


}