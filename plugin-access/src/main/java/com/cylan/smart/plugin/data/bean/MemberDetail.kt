package com.cylan.smart.plugin.data.bean

import com.cylan.smart.base.BaseResponse

/**
 *
 * @author yanzhendong
 * @since 2019/4/15 下午1:49
 */
@Suppress("unused")
data class MemberDetail(

    var count: Long = 0,
    var members: List<Member> = arrayListOf(),
    var member_faces: List<MemberFace> = arrayListOf()
) :BaseResponse()


//MemberId         string `json:"member_id" orm:"column(member_id);pk;unique;"`           //会员Id
//BrandId          string `json:"brand_id" orm:"column(brand_id);size(255);"`             //品牌id
//ShopId           string `json:"shop_id" orm:"column(shop_id);size(255);"`               //所属店铺
//FaceRepositoryId string `json:"face_repository_id" orm:"column(face_repository_id);"`   //脸库id
//MemberName       string `json:"member_name" orm:"column(member_name);size(255);"`       //用户名称
//MemberTypeData       int64  `json:"member_type" orm:"column(member_type);"`                 //会员类型(普通会员、白金会员)
//ClientManager    string `json:"client_manager" orm:"column(client_manager);"`           //客户经理
//Mobile           string `json:"mobile" orm:"column(mobile);size(255);"`                 //手机号
//Sex              string `json:"sex" orm:"column(sex);"`                                 //性别(male,female)
//Birthday         string `json:"birthday" orm:"column(birthday);"`                       //出生日期（如2018-04-17）
//MemberCard       string `json:"member_card" orm:"column(member_card);size(255);"`       //会员卡号
//CreateTime       int64  `json:"create_time" orm:"column(create_time);"`                 //创建时间(秒的时间戳)
//MemberAddress    string `json:"member_address" orm:"column(member_address);size(255);"` //会员详细地址
//CardBalance 	   string `json:"card_balance" orm:"column(card_balance);size(255);"`		  //卡内余额
//AmountSpentMonth string	`json:"amount_spent_month" orm:"column(amount_spent_month);size(255);"` //近一个月消费金额
//StoreNumberMonth string `json:"store_number_month" orm:"column(store_number_month);size(255);"` //近一个月到店次数
//Favorite 		     string	`json:"favorite" orm:"column(favorite);size(255);"`
data class Member(
    var member_id: String = "",
    var brand_id: String = "",
    var shop_id: String = "",
    var face_repository_id: String = "",
    var member_name: String = "",
    var member_type_id: String = "",
    var client_manager: String = "",
    var mobile: String = "",
    var sex: String = "",
    var birthday: String = "",
    var member_card: String = "",
    var create_time: Long,
    var member_address: String = "",
    var card_balance: String = "",
    var amount_spent_month: String = "",
    var store_number_month: String = "",
    var favorite: String = "",
    var face_url: String = ""
)

data class MemberFace(
    var id: String = "",
    var member_id: String = "",
    var face_url: String = ""
)
/*
  上传头像返回的脸库地址信息
 */
data class FaceUrl(
    var code: Int,
    var msg: String = "",
    var request_id: String = "",
    var icon_url: String
)
