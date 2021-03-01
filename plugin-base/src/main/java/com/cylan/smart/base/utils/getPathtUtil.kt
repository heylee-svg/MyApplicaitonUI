package com.cylan.smart.base.utils

import android.annotation.TargetApi
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.loader.content.CursorLoader

object getPathtUtil {


    //简易处理板  （实际本没有发现什么问题，可以直接使用）
    fun getRealPathFromURI(context: Context, contentURI: Uri): String? {
        val result: String?
        val cursor = context.contentResolver.query(
            contentURI,
            arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null
        )//
        if (cursor == null)
            result = contentURI.path
        else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(index)
            cursor.close()
        }
        return result
    }

    //复杂版处理  (适配多种API)   最后直接调用这个方法就可以了
    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        val sdkVersion = Build.VERSION.SDK_INT
        if (sdkVersion < 11) return getRealPathFromUri_BelowApi11(context, uri)
        return if (sdkVersion < 19)
            getRealPathFromUri_Api11To18(context, uri)
        else
            getRealPathFromUri_AboveApi19(context, uri)
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun getRealPathFromUri_AboveApi19(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val wholeID = DocumentsContract.getDocumentId(uri)


        // 使用':'分割
        val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media._ID + "=?"
        val selectionArgs = arrayOf(id)

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, //
            projection, selection, selectionArgs, null
        )
        val columnIndex = cursor!!.getColumnIndex(projection[0])
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex)
        cursor.close()
        return filePath
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    private fun getRealPathFromUri_Api11To18(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        //这个有两个包不知道是哪个。。。。不过这个复杂版一般用不到
        val loader = CursorLoader(context, uri, projection, null, null, null)
        val cursor = loader.loadInBackground()

        if (cursor != null) {
            cursor.moveToFirst()
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]))
            cursor.close()
        }
        return filePath
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    private fun getRealPathFromUri_BelowApi11(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]))
            cursor.close()
        }
        return filePath
    }


}
