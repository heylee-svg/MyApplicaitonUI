package com.cylan.smart.plugin.api

import com.cylan.smart.base.BaseResponse
import com.cylan.smart.base.ListResponse
import com.cylan.smart.plugin.data.bean.*
import com.cylan.smart.plugin.data.request.DoorAccessInfoRequest
import com.cylan.smart.plugin.data.request.DoorAccessListRequest
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author Lupy create on 19-1-14
 * @Description
 */

interface HomeService {



    @POST("api/smartshop/v1/restful/visitor/shop/chef")
    fun getChefList(@Body param:RequestBody):Observable<ListResponse<CaptureListItem>>

    /**
     * 获取考勤人员信息
     */
    @POST("api/smartshop/v1/member/get/info/by")
    fun getMemberInfoDetail(@Body params: RequestBody): Observable<MemberDetail>

    /**
     * 门禁记录请求
     */
    @POST("api/smartshop/v1/restful/access_control/shop/list")
    fun getDoorAccessList(@Body params:DoorAccessListRequest):Observable<ListResponse<DoorAccess>>

    /**
     * 门禁已识别人员记录详情
     */
    @POST("api/smartshop/v1/restful/access_control/shop/info")
    fun getDoorAccessInfo(@Body params: DoorAccessInfoRequest):Observable<ListResponse<DoorAccess>>

    /**
     * 获取考勤规则
     */

    @POST("api/smartshop/v1/restful/attendance/rule/get")
    fun getKaoqinInfo(@Body params: RequestBody): Observable<KaoqinRule>

    /**
     * 编辑考勤规则
     */
    @POST("api/smartshop/v1/restful/attendance/rule/edit")
    fun editKaoqinInfo(@Body params:RequestBody):Observable<BaseResponse>
    /**
     * 设置考勤规则
     */
    @POST("api/smartshop/v1/restful/attendance/rule/set")
    fun setKaoqinInfo(@Body params:RequestBody):Observable<BaseResponse>

    /**
     * 天考勤数据
     */
    @POST("api/smartshop/v1/restful/attendance/shop/day_general")
    fun getDayKaoqinInfo(@Body params: RequestBody):Observable<DayKaoqinInfo>

    /**
     * 月考勤数据
     */
    @POST("api/smartshop/v1/restful/attendance/shop/month_general")
    fun getMonthKaoqinInfo(@Body params: RequestBody):Observable<KaoqinTotal>


    /**
     * 当月所有员工考勤数据
     */
    @POST("api/smartshop/v1/restful/attendance/shop/month_list_all")
    fun getKaoqinListByMonth(@Body params: RequestBody):Observable<ListResponse<KaoqinInfo>>


    /**
     * 单个员工考勤数据
     */
    @POST("api/smartshop/v1/restful/attendance/shop/month_list_one")
    fun getPersonalKaoqinByMonth(@Body params: RequestBody):Observable<KaoqinPersonData>

}