package weathercli

import io.ktor.client.*
import io.ktor.client.engine.curl.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.json.Json
import platform.posix.mkdir

actual fun buildHttpClient(json: Json): HttpClient = HttpClient(Curl) {
    install(ContentNegotiation) { json(json) }
}

@OptIn(ExperimentalForeignApi::class)
actual fun makeDir(path: String) { mkdir(path, 0b111_101_101u) }

