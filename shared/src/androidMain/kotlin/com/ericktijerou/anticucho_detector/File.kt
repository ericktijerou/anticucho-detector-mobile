package com.ericktijerou.anticucho_detector

import java.io.File

actual fun readFile(path: String): ByteArray = File(path).readBytes()