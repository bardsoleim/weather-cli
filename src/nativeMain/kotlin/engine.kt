package weathercli

import io.ktor.client.*
import kotlinx.serialization.json.Json

expect fun buildHttpClient(json: Json): HttpClient
expect fun makeDir(path: String)

