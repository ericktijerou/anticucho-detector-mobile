package com.ericktijerou.anticucho_detector

import kotlinx.cinterop.readBytes
import platform.Foundation.NSData
import platform.Foundation.create

@ExperimentalUnsignedTypes
actual fun readFile(path: String): ByteArray {
    val data = NSData.create(contentsOfFile = path) ?: throw Exception("File not found!")
    return data.bytes?.readBytes(data.length.toInt()) ?: throw Exception("Read bytes error found!")
}