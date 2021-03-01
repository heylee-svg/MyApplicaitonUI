package com.cylan.smart.plugin.data.bean

import android.os.Parcel
import android.os.Parcelable

/**
 *@author Lupy Create on 2019-08-14.
 *@description 门禁记录
 */
data class DoorAccess(
        //报警时间
        var time: Long,
        //人物标识
        var person_id: String,
        //人物姓名
        var person_name: String?,
        //人物类别 0-未注册 1-已注册 2-员工
        var person_type: Int,
        //脸图地址
        var face_url: String?,
        //设备名称
        var camera_name: String?
): Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString()!!,
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()
    ) {
    }

    override fun toString(): String {
        return "DoorAccess(time=$time, person_id='$person_id', person_name='$person_name', person_type=$person_type, face_url='$face_url', camera_name='$camera_name')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(time)
        parcel.writeString(person_id)
        parcel.writeString(person_name)
        parcel.writeInt(person_type)
        parcel.writeString(face_url)
        parcel.writeString(camera_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DoorAccess> {
        override fun createFromParcel(parcel: Parcel): DoorAccess {
            return DoorAccess(parcel)
        }

        override fun newArray(size: Int): Array<DoorAccess?> {
            return arrayOfNulls(size)
        }
    }
}