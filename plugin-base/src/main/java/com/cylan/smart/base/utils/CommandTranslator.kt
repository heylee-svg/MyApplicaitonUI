package com.cylan.smart.base.utils

import android.util.Log
import com.cylan.smart.base.constant.TransmitterHeader
import okio.ByteString
import java.nio.ByteBuffer

/**
 *
 * @author yanzhendong
 * @since 2019/1/10 下午3:35
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
object CommandTranslator {
    private const val BUFFER_SIZE = 1024
    private const val CID_LENGTH = 16
    private val EMPTY_CID = ByteArray(CID_LENGTH)

    private const val TYPE_SHORT = 0
    private const val TYPE_INT = 1
    private const val TYPE_LONG = 2
    private const val TYPE_BYTE = 3
    private const val TYPE_DOUBLE = 4
    private const val TYPE_CHAR = 5

    const val JFG_MSG_MAGIC: Short = 0x4D4A
    const val JFG_MSG_PING: Short = 0x0000
    const val JFG_MSG_F_PING: Short = 0x1004
    fun encodePing(): ByteString {
        return encode(JFG_MSG_MAGIC, JFG_MSG_PING, EMPTY_CID)
    }

    fun encodeFPing(): ByteString {
        return encode(JFG_MSG_MAGIC, JFG_MSG_F_PING, EMPTY_CID)
    }

    private fun encode(vararg fields: Any): ByteString {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE)
        //todo 如果 buffer size 不够用 自动调整 buffer 大小
        for (field in fields) {
            when (field) {
                is Short -> buffer.putShort(field)
                is Int -> buffer.putInt(field)
                is Long -> buffer.putLong(field)
                is Byte -> buffer.put(field)
                is Double -> buffer.putDouble(field)
                is Char -> buffer.putChar(field)
                is ShortArray -> buffer.asShortBuffer().put(field)
                is IntArray -> buffer.asIntBuffer().put(field)
                is LongArray -> buffer.asLongBuffer().put(field)
                is ByteArray -> buffer.put(field)
                is DoubleArray -> buffer.asDoubleBuffer().put(field)
                is CharArray -> buffer.asCharBuffer().put(field)
                is String -> buffer.put(field.toByteArray())
                is Array<*> -> {
                    //todo
                }
            }
        }
        Log.i("TransmitterServiceImpl", "byteCount:" + buffer.position())
        buffer.limit(buffer.position()).rewind()
        return ByteString.of(buffer)
    }

    fun decodeHeader(content: ByteString): TransmitterHeader {
        val buffer = content.asByteBuffer()
        val cidBytes = ByteArray(CID_LENGTH)
        return TransmitterHeader(
            buffer.short,
            buffer.short,
            buffer.get(cidBytes).run { String(cidBytes).trim { it < ' ' } })
    }

    fun decodeString() {

    }

    private fun decode(content: ByteString) {

    }
}