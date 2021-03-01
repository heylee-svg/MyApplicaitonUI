package com.cylan.smart.base

import com.google.gson.annotations.SerializedName

/**
 * @author Lupy create on 19-1-18
 * @Description 列表数据返回数据结构
 */

data class ListResponse<T>(
    var count: String,
    @SerializedName(value = "datas", alternate = ["data", "device_infos"])
    var datas: List<T>
) : BaseResponse() {
    var person_info: PersonInfoFace? = null
}

data class PersonInfoFace(
    var face_repository_id: String = "",
    var face_repository_name: String = ""
)