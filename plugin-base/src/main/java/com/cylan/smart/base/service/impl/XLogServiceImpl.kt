package com.cylan.smart.base.service.impl

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.os.Environment
import com.alibaba.android.arouter.facade.annotation.Route
import com.cylan.smart.base.service.ILoggerService
import com.cylan.smart.base.utils.AppContext
import com.cylan.smart.base.utils.PathUtils
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.Flattener2
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy
import com.elvishew.xlog.printer.file.naming.FileNameGenerator
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author yanzhendong
 * @since 2019/1/11 下午3:50
 */
@Suppress("unused")
@Route(path = "/base/logger_service", name = "日志打印服务")
class XLogServiceImpl : ILoggerService {
    override fun verbose(message: String) {
        XLog.v(message)
    }

    override fun debug(message: String) {
        XLog.d(message)
    }

    override fun info(message: String) {
        XLog.i(message)
    }

    override fun warning(message: String) {
        XLog.w(message)
    }

    override fun error(message: String) {
        XLog.e(message)
    }

    override fun verbose(tag: String, message: String) {
        XLog.v(LOG_FORMAT, tag, message)
    }

    override fun warning(tag: String, message: String) {
        XLog.w(LOG_FORMAT, tag, message)
    }

    override fun info(tag: String, message: String) {
        XLog.i(LOG_FORMAT, tag, message)
    }

    override fun error(tag: String, message: String) {
        XLog.e(LOG_FORMAT, tag, message)
    }

    override fun debug(tag: String, message: String) {
        XLog.d(LOG_FORMAT, tag, message)
    }

    override fun init(context: Context?) {
        val logConfiguration = LogConfiguration.Builder()
            .logLevel(LogLevel.ALL)
            .tag(APP_TAG)
            .t()
            .build()
        XLog.init(logConfiguration, AndroidPrinter(), SafeFilePrinter())
    }

    private class SafeFilePrinter : Printer {
        private var mHasStoragePermission: Boolean =
            EasyPermissions.hasPermissions(AppContext.getContext(), WRITE_EXTERNAL_STORAGE)
        private val mFilePrinter: FilePrinter

        init {
            mFilePrinter = FilePrinter.Builder(PathUtils.APP_LOGGER_DIR)
                .backupStrategy(FileSizeBackupStrategy(MAX_LOG_SIZE))
                .flattener(object : Flattener2 {
                    private val timeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    override fun flatten(
                        timeMillis: Long,
                        logLevel: Int,
                        tag: String?,
                        message: String?
                    ): CharSequence {
                        return "[${timeFormatter.format(timeMillis)}-$tag]:$message"
                    }
                })
                .fileNameGenerator(object : FileNameGenerator {
                    private val fileNameFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    override fun generateFileName(logLevel: Int, timestamp: Long): String {
                        return "$APP_TAG-${fileNameFormatter.format(timestamp)}.txt"
                    }

                    override fun isFileNameChangeable() = true
                })
                .build()
        }

        override fun println(logLevel: Int, tag: String?, msg: String?) {
            if (isSafe()) {
                mFilePrinter.println(logLevel, tag, msg)
            }
        }

        private fun isSafe(): Boolean {
            return mHasStoragePermission
        }
    }

    companion object {
        private const val APP_TAG = "SmartApp"
        private const val LOG_FORMAT = "[%s]=>%s"
        private const val MAX_LOG_SIZE = 1024 * 1024 * 32L
    }
}