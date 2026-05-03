package weathercli

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

private const val GEO_BASE     = "https://geocoding-api.open-meteo.com/v1/search"
private const val WEATHER_BASE = "https://api.open-meteo.com/v1/forecast"

/**
 * Resolves a city name to a [GeoResult] using the Open-Meteo Geocoding API.
 * Returns `null` if the city is not found or the request fails.
 */
fun geocodeCity(client: HttpClient, json: Json, query: String): GeoResult? {
    val encoded = query.replace(" ", "+")
    val url = "$GEO_BASE?name=$encoded&count=1&language=en&format=json"

    val body = runBlocking {
        val response: HttpResponse = client.get(url)
        check(response.status.value == 200) {
            "Geocoding request failed with HTTP ${response.status.value}"
        }
        response.bodyAsText()
    }

    return json.decodeFromString<GeoResponse>(body).results?.firstOrNull()
}

/**
 * Fetches the weather forecast for the given coordinates and day offset.
 * [dayOffset] 0 = today, 1 = tomorrow, etc.
 */
fun fetchWeather(client: HttpClient, json: Json, lat: Double, lon: Double, dayOffset: Int): WeatherResponse {
    val forecastDays = dayOffset + 1

    // Only request current conditions when showing today
    val currentParams = if (dayOffset == 0)
        "&current=temperature_2m,apparent_temperature,relative_humidity_2m," +
        "wind_speed_10m,wind_direction_10m,weather_code,precipitation,surface_pressure"
    else ""

    val url = "$WEATHER_BASE" +
        "?latitude=$lat&longitude=$lon" +
        "&hourly=temperature_2m,apparent_temperature,precipitation_probability," +
        "precipitation,weather_code,wind_speed_10m,wind_direction_10m" +
        currentParams +
        "&wind_speed_unit=ms" +
        "&forecast_days=$forecastDays" +
        "&timezone=auto"

    val body = runBlocking {
        val response: HttpResponse = client.get(url)
        check(response.status.value == 200) {
            "Weather request failed with HTTP ${response.status.value}"
        }
        response.bodyAsText()
    }

    return json.decodeFromString<WeatherResponse>(body)
}

