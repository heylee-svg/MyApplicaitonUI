package com.cylan.smart.base

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.base.service.ILoggerService
import com.cylan.smart.base.service.INetStateService
import com.cylan.smart.base.service.IPushService
import com.cylan.smart.base.service.IStorageService
import com.google.gson.JsonArray
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.internal.Util
import org.json.JSONArray
import org.json.JSONObject
import java.io.Closeable
import java.io.File
import java.nio.charset.Charset

/**
 * @author Lupy create on 19-1-17
 * @Description
 */
var storage: IStorageService? = null
    get() {
        field = field ?: ARouter.getInstance().navigation(IStorageService::class.java)
        return field
    }

var logger: ILoggerService? = null
    get() {
        if (field == null) {
            field = ARouter.getInstance().navigation(ILoggerService::class.java)
        }
        return field
    }

var mqtt: IPushService? = null
    get() {
        field = field ?: ARouter.getInstance().navigation(IPushService::class.java)
        return field
    }

var netState: INetStateService? = null
    get() {
        field = field ?: ARouter.getInstance().navigation(INetStateService::class.java)
        return field
    }
/**
 * 通过键值对来构造RequestBody
 */
fun getRequestBodyByPair(vararg params: Pair<String, Any?>): RequestBody {

    var json = JSONObject()
    json.put("industry", storage?.getInt(ConstantField.BRANDS_SELECT, 0))  //适配服务端坑爹的接口
    for (pair in params) {
        json.put(pair.first, pair.second)

    }
    return RequestBody.create(
        okhttp3.MediaType.parse("application/json; charset=utf-8"),
       json.toString()
    )
}

/**
 * 通过JSONObject来生成一个
 */

fun convertRequestBodyToArr(contentType: MediaType?, content: String): ByteArray {

    var charset: Charset? = Util.UTF_8
    if (contentType != null) {
        charset = contentType.charset()
        if (charset == null) {
            charset = Util.UTF_8
//            contentType = MediaType.parse("$contentType; charset=utf-8")
        }
    }
    val bytes = content.toByteArray(charset!!)

    return bytes

}

fun createRequestBody(byte:ByteArray ):RequestBody{
    return RequestBody.create(
        okhttp3.MediaType.parse("application/json; charset=utf-8"), byte)
}



fun String.dir(): String = try {
    File(this).mkdirs()
    this
} catch (e: Exception) {
    logger?.error(e.message!!)
    this
}

fun String.pdir(): String = try {
    File(this).parentFile.mkdirs()
    this
} catch (e: Exception) {
    logger?.error(e.message!!)
    this
}

fun String.exist() = File(this).exists()

fun Closeable.closeQuietly() = try {
    close()
} catch (e: Exception) {
    logger?.error(e.message!!)
}

fun ImageView.load(src: String): ImageView {
    val key = try {
        Uri.parse(src).path
    } catch (e: Exception) {
        src
    }
    return Glide.with(this).load(src).apply(
        RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            .signature(ObjectKey(key!!))
    ).into(this)
        .let { this }
}

fun String.preview(): String =
    storage?.getString("${ConstantField.PREVIEW_PICTURE}_$this", "") ?: ""

fun EditText.bindClearIcon(clearIcon: ImageView) {

    clearIcon?.setOnClickListener {
        this.setText("")
    }

    this.setOnFocusChangeListener { v, hasFocus ->
        if (hasFocus) {
            var target = v as EditText
            if (target.text.isNotBlank() && target.text.isNotEmpty()) {
                clearIcon?.visibility = View.VISIBLE
            }
        } else {
            clearIcon?.visibility = View.INVISIBLE
        }
    }

    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearIcon?.visibility = if (TextUtils.isEmpty(s)) View.INVISIBLE else View.VISIBLE
        }
    })
}

/**
 * 隐藏软键盘
 */
fun Activity.hideInputMethod() {
    var inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    currentFocus?.apply { inputManager?.hideSoftInputFromWindow(windowToken, 0) }
}

/**
 * 显示软键盘
 */
fun Activity.showInputMethod(view: View) {
    if (view is EditText) {
        var inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.showSoftInput(view, 0)
    }
}