package com.cylan.smart.base.utils

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 *
 * @author yanzhendong
 * @since 2019/1/11 上午9:31
 */
@Suppress("unused")
object ShellUtils {
    private val LINE_SEP = System.getProperty("line.separator")

    /**
     * 是否是在 root 下执行命令
     *
     * @param command 命令
     * @param isRoot  是否需要 root 权限执行
     * @return CommandResult
     */
    fun execCmd(command: String, isRoot: Boolean): CommandResult {
        return execCmd(arrayOf(command), isRoot, true)
    }

    /**
     * 是否是在 root 下执行命令
     *
     * @param commands 多条命令链表
     * @param isRoot   是否需要 root 权限执行
     * @return CommandResult
     */
    fun execCmd(commands: List<String>?, isRoot: Boolean): CommandResult {
        return execCmd(commands?.toTypedArray(), isRoot, true)
    }

    /**
     * 是否是在 root 下执行命令
     *
     * @param commands 多条命令数组
     * @param isRoot   是否需要 root 权限执行
     * @return CommandResult
     */
    fun execCmd(commands: Array<String>, isRoot: Boolean): CommandResult {
        return execCmd(commands, isRoot, true)
    }

    /**
     * 是否是在 root 下执行命令
     *
     * @param command         命令
     * @param isRoot          是否需要 root 权限执行
     * @param isNeedResultMsg 是否需要结果消息
     * @return CommandResult
     */
    fun execCmd(command: String, isRoot: Boolean, isNeedResultMsg: Boolean): CommandResult {
        return execCmd(arrayOf(command), isRoot, isNeedResultMsg)
    }

    /**
     * 是否是在 root 下执行命令
     *
     * @param commands        命令链表
     * @param isRoot          是否需要 root 权限执行
     * @param isNeedResultMsg 是否需要结果消息
     * @return CommandResult
     */
    fun execCmd(commands: List<String>?, isRoot: Boolean, isNeedResultMsg: Boolean): CommandResult {
        return execCmd(commands?.toTypedArray(), isRoot, isNeedResultMsg)
    }

    /**
     * 是否是在 root 下执行命令
     *
     * @param commands        命令数组
     * @param isRoot          是否需要 root 权限执行
     * @param isNeedResultMsg 是否需要结果消息
     * @return CommandResult
     */
    fun execCmd(commands: Array<String>?, isRoot: Boolean, isNeedResultMsg: Boolean): CommandResult {
        var result = -1
        if (commands == null || commands.isEmpty()) {
            return CommandResult(result, null, null)
        }
        var process: Process? = null
        var successResult: BufferedReader? = null
        var errorResult: BufferedReader? = null
        var successMsg: StringBuilder? = null
        var errorMsg: StringBuilder? = null
        var os: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec(if (isRoot) "su" else "sh")
            os = DataOutputStream(process!!.outputStream)
            for (command in commands) {
                os.write(command.toByteArray())
                os.writeBytes(LINE_SEP)
                os.flush()
            }
            os.writeBytes("exit$LINE_SEP")
            os.flush()
            result = process.waitFor()
            if (isNeedResultMsg) {
                successMsg = StringBuilder()
                errorMsg = StringBuilder()
                successResult = BufferedReader(InputStreamReader(process.inputStream, "UTF-8"))
                errorResult = BufferedReader(InputStreamReader(process.errorStream, "UTF-8"))
                var line: String? = successResult.readLine()
                while (line != null) {
                    successMsg.append(line).append(LINE_SEP)
                    line = successResult.readLine()
                }
                line = errorResult.readLine()
                while (line != null) {
                    errorMsg.append(line).append(LINE_SEP)
                    line = errorResult.readLine()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
                successResult?.close()
                errorResult?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            process?.destroy()
        }
        return CommandResult(
            result,
            successMsg?.toString(),
            errorMsg?.toString()
        )
    }

    /**
     * 返回的命令结果
     */
    class CommandResult(
        /**
         * 结果码
         */
        var result: Int = 0,
        /**
         * 成功信息
         */
        var successMsg: String? = "",
        /**
         * 错误信息
         */
        var errorMsg: String? = ""
    )
}