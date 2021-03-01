package com.cylan.smart.plugin.data

import com.cylan.smart.base.*
import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.plugin.api.HomeService
import com.cylan.smart.plugin.data.bean.*
import com.cylan.smart.plugin.data.request.DoorAccessInfoRequest
import com.cylan.smart.plugin.data.request.DoorAccessListRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Lupy create on 19-1-14
 * @Description 负责网络数据的获取
 */

class DataManger {

    private var service: HomeService

    companion object {
        private val dataManger: DataManger by lazy<DataManger> {
            DataManger()
        }

        fun getInstance(): DataManger {
            return dataManger
        }
    }

    init {
        service = RetrofitCore.service(HomeService::class.java)


    }


    fun getChefList(
            shopId: String,
            brandId: String,
            offset: Int,
            listener: (List<CaptureListItem>?, Int) -> Unit
    ) {
        val token = storage?.getString(ConstantField.TOKEN, "") ?: ""
        val account = storage?.getString(ConstantField.ACCOUNT, "") ?: ""
        var dis = service.getChefList(
                getRequestBodyByPair(
                        "auth_token" to token,
                        "account" to account,
                        "time" to System.currentTimeMillis() / 1000,
                        "shop_id" to shopId,
                        "brand_id" to brandId,
                        "limit" to 200,
                        "offset" to offset
                )
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DataConsume {
                    if (it.code == 200) {
                        listener.invoke(it.datas, it.code!!)
                    } else {
                        listener.invoke(null, it.code!!)
                    }
                    false
                }, NetError { a, i ->
                    listener.invoke(null, i)
                })

    }

    fun getMemberDetail(memberId: String, listener: (MemberDetail?, Int?) -> Unit) {
        val token = storage?.getString(ConstantField.TOKEN, "") ?: ""
        val account = storage?.getString(ConstantField.ACCOUNT, "") ?: ""
        service.getMemberInfoDetail(
                getRequestBodyByPair(
                        "member_id" to memberId,
                        "auth_token" to token,
                        "account" to account,
                        "time" to System.currentTimeMillis() / 1000
                )
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DataConsume {
                    if (it.code == 200) {
                        listener.invoke(it, it.code)
                    } else {
                        listener.invoke(null, it.code)
                    }
                    false
                }, NetError({ a, i ->
                    listener.invoke(null, i)
                }))

    }


    fun setKaoqinInfo(
            am: String?,
            pm: String?,
            weekend: Int?,
            enable: Boolean?,
            listener: (BaseResponse, Int) -> Unit
    ) {
        var token = storage?.getString(ConstantField.TOKEN, "")
        var account = storage?.getString(ConstantField.ACCOUNT, "")
        var brandId = storage?.getString(ConstantField.BRAND_ID, "")
        var shopId = storage?.getString(ConstantField.SHOP_ID, "")
        if (token.isNullOrEmpty() || account.isNullOrEmpty()) {
            logger?.debug("token or accoutn is null for getShipInfoList")
            BroadcastManager.instance.sendAuthTokeninvalidate()
            return
        }
        service.setKaoqinInfo(
                getRequestBodyByPair(
                        "time" to System.currentTimeMillis() / 1000,
                        "auth_token" to token,
                        "account" to account,
                        "brand_id" to brandId,
                        "shop_id" to shopId,
                        "work_time_am" to am,
                        "work_time_pm" to pm,
                        "work_week" to weekend,
                        "enable" to enable
                )
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DataConsume {
                    listener.invoke(it, it.code!!)
                    false
                }, NetError(listener as (Any?, Int) -> Unit))
    }

    fun editKaoqinInfo(
            am: String?,
            pm: String?,
            weekend: Int?,
            enable: Boolean?,
            listener: (BaseResponse?, Int?) -> Unit
    ) {
        var token = storage?.getString(ConstantField.TOKEN, "")
        var account = storage?.getString(ConstantField.ACCOUNT, "")
        var brandId = storage?.getString(ConstantField.BRAND_ID, "")
        var shopId = storage?.getString(ConstantField.SHOP_ID, "")
        if (token.isNullOrEmpty() || account.isNullOrEmpty()) {
            logger?.debug("token or accoutn is null for getShipInfoList")
            BroadcastManager.instance.sendAuthTokeninvalidate()
            return
        }
        service.editKaoqinInfo(
                getRequestBodyByPair(
                        "time" to System.currentTimeMillis() / 1000,
                        "auth_token" to token,
                        "account" to account,
                        "brand_id" to brandId,
                        "shop_id" to shopId,
                        "work_time_am" to am,
                        "work_time_pm" to pm,
                        "work_week" to weekend,
                        "enable" to enable
                )
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DataConsume {
                    if (it != null) {
                        listener.invoke(it, it.code)

                    }
                    false
                }, NetError(listener as (Any?, Int) -> Unit))
    }


    /**
     * 获取考勤信息
     */

    fun getKaoqinInfo(listener: (data: KaoqinRule?, status: Int) -> Unit) {
        var token = storage?.getString(ConstantField.TOKEN, "")
        var account = storage?.getString(ConstantField.ACCOUNT, "")
        var brandId = storage?.getString(ConstantField.BRAND_ID, "")
        var shopId = storage?.getString(ConstantField.SHOP_ID, "")

        if (token.isNullOrEmpty() || account.isNullOrEmpty()) {
            logger?.debug("token or accoutn is null for getShipInfoList")
            BroadcastManager.instance.sendAuthTokeninvalidate()
            return
        }
        service.getKaoqinInfo(
                getRequestBodyByPair(
                        "time" to System.currentTimeMillis() / 1000,
                        "auth_token" to token,
                        "account" to account,
                        "brand_id" to brandId!!,
                        "shop_id" to shopId!!
                )
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DataConsume {
                    if (it.code == 200) {
                        listener.invoke(it, 200)

                    } else {
                        listener.invoke(null, it.code)
                    }
                    false
                }, NetError(listener as (Any?, Int) -> Unit))
    }


    fun getDoorAccessList(
            request: DoorAccessListRequest,
            listener: (List<DoorAccess>?, Int) -> Unit
    ) {
        var dis = service.getDoorAccessList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DataConsume {
                    if (it.code == 200) {
                        listener.invoke(it.datas, it.code!!)
                    } else {
                        listener.invoke(null, it.code!!)
                    }
                    false
                }, NetError { a, i ->
                    listener.invoke(null, i)
                })
    }

    fun getDoorAccessInfo(
            request: DoorAccessInfoRequest,
            listener: (ListResponse<DoorAccess>?, Int) -> Unit
    ) {
        var disp = service.getDoorAccessInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DataConsume {
                    if (it.code == 200) {
                        listener.invoke(it, it.code!!)
                    } else {
                        listener.invoke(null, it.code!!)
                    }
                    false
                }, NetError { a, i ->
                    listener.invoke(null, i)
                })
    }

    fun getDayKaoqinInfo(
            day: String,
            listener: (DayKaoqinInfo?, Int?) -> Unit
    ) {
        val token = storage?.getString(ConstantField.TOKEN, "")
        val account = storage?.getString(ConstantField.ACCOUNT, "")
        val brand_id = storage?.getString(ConstantField.BRAND_ID, "")
        val shop_id = storage?.getString(ConstantField.SHOP_ID, "")
        service.getDayKaoqinInfo(
                getRequestBodyByPair(
                        "auth_token" to token,
                        "account" to account,
                        "time" to System.currentTimeMillis() / 1000,
                        "brand_id" to brand_id,
                        "shop_id" to shop_id,
                        "day" to day
                )
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DataConsume {
                    if (it != null) {
                        listener.invoke(it, it.code)
                    }
                    false
                }, NetError({ a, i ->
                    listener.invoke(null, i)
                }))
    }

    fun getMonthKaoqinInfo(
            month: String,
            listener: (KaoqinTotal?, Int?) -> Unit
    ) {
        val token = storage?.getString(ConstantField.TOKEN, "")
        val account = storage?.getString(ConstantField.ACCOUNT, "")
        val brand_id = storage?.getString(ConstantField.BRAND_ID, "")
        val shop_id = storage?.getString(ConstantField.SHOP_ID, "")
        service.getMonthKaoqinInfo(
                getRequestBodyByPair(
                        "auth_token" to token,
                        "account" to account,
                        "time" to System.currentTimeMillis() / 1000,
                        "brand_id" to brand_id,
                        "shop_id" to shop_id,
                        "month" to month
                )
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DataConsume {
                    if (it != null) {
                        listener.invoke(it, it.code)
                    }
                    false
                }, NetError({ a, i ->
                    listener.invoke(null, i)
                }))

    }

    fun getKaoqinListByMonth(
            month: String,
            listener: (ListResponse<KaoqinInfo>?, Int?) -> Unit
    ) {
        val token = storage?.getString(ConstantField.TOKEN, "")
        val account = storage?.getString(ConstantField.ACCOUNT, "")
        val brand_id = storage?.getString(ConstantField.BRAND_ID, "")
        val shop_id = storage?.getString(ConstantField.SHOP_ID, "")
        service.getKaoqinListByMonth(
                getRequestBodyByPair(
                        "auth_token" to token,
                        "account" to account,
                        "time" to System.currentTimeMillis() / 1000,
                        "brand_id" to brand_id,
                        "shop_id" to shop_id,
                        "month" to month
                )
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DataConsume {
                    if (it != null) {
                        listener.invoke(it, it.code)
                    }

                    false
                }, NetError({ a, i ->
                    listener.invoke(null, i)
                }))
    }

    fun getPersonalKaoqinByMonth(
            month: String,
            person_id: String,
            listener: (KaoqinPersonData?, Int?) -> Unit
    ) {
        val token = storage?.getString(ConstantField.TOKEN, "")
        val account = storage?.getString(ConstantField.ACCOUNT, "")
        val brand_id = storage?.getString(ConstantField.BRAND_ID, "")
        val shop_id = storage?.getString(ConstantField.SHOP_ID, "")
        service.getPersonalKaoqinByMonth(
                getRequestBodyByPair(
                        "auth_token" to token,
                        "account" to account,
                        "time" to System.currentTimeMillis() / 1000,
                        "brand_id" to brand_id,
                        "shop_id" to shop_id,
                        "person_id" to person_id,
                        "month" to month

                )
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DataConsume {
                    if (it != null) {
                        listener.invoke(it, it.code)
                    }
                    false
                }, NetError({ a, i ->
                    listener.invoke(null, i)
                }))
    }

}