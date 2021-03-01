package com.cylan.smart.base.utils

import android.os.Environment
import com.cylan.smart.base.dir
import java.io.File

/**
 * @author Lupy create on 19-1-11
 * @Description 获取本地存储路径工具
 */
@Suppress("MemberVisibilityCanBePrivate")
object PathUtils {
    const val ROOT_DIR_NAME = "SmartApp"
    const val LOGGER_DIR_NAME = "Log"
    const val CAPTURE_DIR_NAME = "Captured"
    const val PICTURE_DIR_NAME = "Picture"
    const val CRASH_REPORT_DIR_NAME = "CrashReport"
    val APP_WORKER_DIR: String =
        Environment.getExternalStorageDirectory().absolutePath + File.separator + ROOT_DIR_NAME
        get() = field.dir()

    val APP_LOGGER_DIR: String = APP_WORKER_DIR + File.separator + LOGGER_DIR_NAME
        get() = field.dir()

    val APP_CAPTURE_DIR: String = APP_WORKER_DIR + File.separator + CAPTURE_DIR_NAME
        get() = field.dir()

    val APP_PICTURE_DIR: String = APP_WORKER_DIR + File.separator + PICTURE_DIR_NAME
        get() = field.dir()

    val APP_CRASH_REPORT_DIR: String = APP_WORKER_DIR + File.separator + CRASH_REPORT_DIR_NAME
}


