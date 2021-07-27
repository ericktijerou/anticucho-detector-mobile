package com.ericktijerou.anticucho_detector.remote

import com.ericktijerou.anticucho_detector.readFile
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

@Serializable
data class Response(val result: List<String>)

@Serializable
data class Person(val name: String, val lastname: String)

class AnticuchoDetectorApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://anticucho-detector.herokuapp.com",
) : KoinComponent {
    suspend fun compareImage(filename: String, filepath: String): Response {
        return client.post("$baseUrl/compare") {
            body = buildMultiPartFormData {
                appendFile("file", filename, readFile(filepath))
            }
        }
    }

    fun FormBuilder.appendFile(key: String, fileName: String, data: ByteArray) {
        appendInput(
            key,
            Headers.build { append(HttpHeaders.ContentDisposition, "filename=$fileName") },
            data.size.toLong()
        ) {
            buildPacket { writeFully(data) }
        }
    }

    fun buildMultiPartFormData(function: FormBuilder.() -> Unit) =
        MultiPartFormDataContent(formData(function))
}

/*

body = MultiPartFormDataContent(
formData {
    append(
        key = "file",
        value = byteArray,
        headers = Headers.build {
            append(HttpHeaders.ContentDisposition, "filename=${filename}")
        }
    )

}
)*/
