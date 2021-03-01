@file:Suppress("unused")

package com.cylan.smart.base.constant

import com.cylan.smart.base.utils.CommandTranslator
import okio.ByteString

/**
 *
 * @author yanzhendong
 * @since 2019/1/10 下午7:31
 */
open class TransmitterHeader(var magic: Short = 0, var msgId: Short = 0, var cid: String = "") {
    override fun toString(): String {
        return "TransmitterHeader(magic=$magic, msgId=$msgId, cid='$cid')"
    }

    open fun load(content: ByteString) {
        val byteBuffer = content.asByteBuffer()
        byteBuffer.remaining() > Short.SIZE_BYTES
        val transmitterHeader = CommandTranslator.decodeHeader(content)
    }
}

class PingRsp : TransmitterHeader()

class FPingRsp : TransmitterHeader()