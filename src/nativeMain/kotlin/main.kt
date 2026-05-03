package weathercli

import io.ktor.client.*
import io.ktor.client.engine.curl.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.json.Json
import platform.posix.fprintf
import platform.posix.stderr

private const val DEFAULT_CITY = "Bergen, Norway"
private const val DEFAULT_LAT  = 60.3913
private const val DEFAULT_LON  = 5.3221

@OptIn(ExperimentalForeignApi::class)
private fun err(msg: String) = fprintf(stderr, "%s\n", msg)

fun main(args: Array<String>) {
    val cli = parseArgs(args) ?: return

    val json   = Json { ignoreUnknownKeys = true }
    val client = HttpClient(Curl) {
        install(ContentNegotiation) { json(json) }
    }

    // Resolve location — geocode a custom city, or fall back to Bergen, Norway
    val cityName: String
    val lat: Double
    val lon: Double

    if (cli.cityQuery != null) {
        val result = try {
            geocodeCity(client, json, cli.cityQuery)
        } catch (e: Exception) {
            err("Geocoding error: ${e.message}")
            client.close()
            return
        }

        if (result == null) {
            err("City not found: '${cli.cityQuery}'")
            client.close()
            return
        }

        lat      = result.latitude
        lon      = result.longitude
        cityName = buildString {
            append(result.name)
            if (result.region.isNotEmpty() && result.region != result.name)
                append(", ${result.region}")
            if (result.country.isNotEmpty())
                append(", ${result.country}")
        }
    } else {
        lat      = DEFAULT_LAT
        lon      = DEFAULT_LON
        cityName = DEFAULT_CITY
    }

    // Fetch the forecast
    val weather = try {
        fetchWeather(client, json, lat, lon, cli.dayOffset)
    } catch (e: Exception) {
        err("Weather fetch error: ${e.message}")
        client.close()
        return
    }
    client.close()

    // Determine the target date (last day in the hourly response)
    val targetDate = weather.hourly.time.last().substring(0, 10)

    val label = when (cli.dayOffset) {
        0    -> "Today"
        1    -> "Tomorrow"
        else -> "In ${cli.dayOffset} days"
    }

    // Render output
    printHeader(cityName, label)

    if (cli.dayOffset == 0) {
        printCurrentConditions(weather.current!!, weather.currentUnits!!)
    }

    printHourlyForecast(weather.hourly, targetDate)
}

