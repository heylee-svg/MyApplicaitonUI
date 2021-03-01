package com.cylan.smart.plugin.data.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author Lupy create on 19-1-21
 * @Description　来访数据
 */

data class VisitorData(
    var time: Long,
    var person_id: String?,
    var person_name: String?,
    var person_type: String?,
    var face_url: String?,
    var image_url: String?,
    var register_url: String?,
    var confidence: Float,
    var tag_id: String?,
    var camera_name: String?,
    var gender: String?,//性别，female-女，male-男
    var age: Int,
    var visit_num: Int,
    var member_type: Int
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readFloat(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(time)
        writeString(person_id)
        writeString(person_name)
        writeString(person_type)
        writeString(face_url)
        writeString(image_url)
        writeString(register_url)
        writeFloat(confidence)
        writeString(tag_id)
        writeString(camera_name)
        writeString(gender)
        writeInt(age)
        writeInt(visit_num)
        writeInt(member_type)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VisitorData> = object : Parcelable.Creator<VisitorData> {
            override fun createFromParcel(source: Parcel): VisitorData = VisitorData(source)
            override fun newArray(size: Int): Array<VisitorData?> = arrayOfNulls(size)
        }
    }
}