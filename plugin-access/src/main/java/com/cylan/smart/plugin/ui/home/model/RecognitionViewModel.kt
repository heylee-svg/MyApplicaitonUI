package com.cylan.smart.plugin.ui.home.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.cylan.smart.plugin.data.DataManger
import com.cylan.smart.plugin.res.BR

/**
 * @author Lupy
 * @since 19-6-24
 * @desc $
 */

class RecognitionViewModel : BaseObservable() {

    var shopId: String? = null
        set(value) {
            if (value.isNullOrEmpty() || value.equals(field)) {
                return
            } else {
                field = value
                if (!dateDes.isNullOrEmpty()) {
                    getDataByShopId()
                }
            }
        }

    var brandId: String? = "all"

    /**
     * 1.天　2.周 3.月
     */
    var type: Int = 1


    @Bindable
    var dateDes: String = ""
        set(value) {
            if (!value.isNullOrEmpty() && !value.equals(field)) {
                field = value
                notifyPropertyChanged(BR.dateDes)
                getDataByShopId()
            }
        }

    @Bindable
    var total: String = ""
        set(value) {
            if (!value.isNullOrEmpty() && !value.equals(field)) {
                field = value
                notifyPropertyChanged(BR.total)
            }
        }

    @Bindable
    var maskRate: String = ""
        set(value) {
            if (!value.isNullOrEmpty() && !value.equals(field)) {
                field = value
            }
            notifyPropertyChanged(BR.maskRate)
        }

    @Bindable
    var hatRate: String = ""
        set(value) {
            if (!value.isNullOrEmpty() && !value.equals(field)) {
                field = value
            }
            notifyPropertyChanged(BR.hatRate)
        }

    @Bindable
    var totalRate: String = ""
        set(value) {
            if (!value.isNullOrEmpty() && !value.equals(field)) {
                field = value
            }
            notifyPropertyChanged(BR.totalRate)
        }


    fun getDataByShopId() {
//        getDataByShopId {}
    }

//    fun getDataByShopId(callback: () -> Unit) {
//        shopId?.apply {
//            DataManger.getInstance().getChefDataRate(this, brandId ?: "all", dateDes, dateDes, type) { rateList, status ->
//                callback.invoke()
//                if (status == 200) {
//
//                    var total: Int = 0
//                    var hatTotal: Int = 0
//                    var hatNum: Int = 0
//                    var maskTotal: Int = 0
//                    var maskNum: Int = 0
//                    rateList?.forEach {
//                        total += it.total
//                        hatNum += it.hat_num
//                        hatTotal += it.hat_total
//                        maskNum += it.mask_num
//                        maskTotal += it.mask_total
//                    }
//                    if (total == 0) {
//                        this@RecognitionViewModel.total = "0"
//                        this@RecognitionViewModel.maskRate = "0.00%"
//                        this@RecognitionViewModel.hatRate = "0.00%"
//                        this@RecognitionViewModel.totalRate = "0.00%"
//                    } else {
//                        this@RecognitionViewModel.total = "${total}"
//                        var sum = hatNum.toFloat() + maskNum.toFloat()
//                        if (hatNum != 0 && maskNum != 0) {
//                            sum /= 2
//                        }
//                        this@RecognitionViewModel.totalRate =
//                            String.format("%3.2f%%", sum * 100 / total.toFloat())
//                        if (hatTotal == 0) {
//                            this@RecognitionViewModel.hatRate = "0.00%"
//                        } else {
//                            this@RecognitionViewModel.hatRate =
//                                String.format("%3.2f%%", (hatNum.toFloat() * 100 / hatTotal.toFloat()))
//                        }
//
//                        if (maskTotal == 0) {
//                            this@RecognitionViewModel.maskRate = "0.00%"
//                        } else {
//                            this@RecognitionViewModel.maskRate =
//                                String.format("%3.2f%%", (maskNum.toFloat() * 100 / maskTotal.toFloat()))
//                        }
//                    }
//                } else {
//
//                }
//            }
//        }
//    }

}