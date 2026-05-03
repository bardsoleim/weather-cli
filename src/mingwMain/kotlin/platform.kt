package weathercli

import io.ktor.client.*
import io.ktor.client.engine.winhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.json.Json
import platform.windows.CreateDirectoryW

actual fun buildHttpClient(json: Json): HttpClient = HttpClient(WinHttp) {
    install(ContentNegotiation) { json(json) }
}

@OptIn(ExperimentalForeignApi::class)
actual fun makeDir(path: String) { CreateDirectoryW(path, null) }

