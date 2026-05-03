package weathercli

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ── Geocoding ─────────────────────────────────────────────────────────────────

@Serializable
data class GeoResult(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @SerialName("country") val country: String = "",
    @SerialName("admin1")  val region: String  = "",
)

@Serializable
data class GeoResponse(
    val results: List<GeoResult>? = null,
)

// ── Weather ───────────────────────────────────────────────────────────────────

@Serializable
data class CurrentWeather(
    val time: String,
    @SerialName("temperature_2m")       val temperature: Double,
    @SerialName("apparent_temperature") val apparentTemperature: Double,
    @SerialName("relative_humidity_2m") val humidity: Int,
    @SerialName("wind_speed_10m")       val windSpeed: Double,
    @SerialName("wind_direction_10m")   val windDirection: Int,
    @SerialName("weather_code")         val weatherCode: Int,
    val precipitation: Double,
    @SerialName("surface_pressure")     val pressure: Double,
)

@Serializable
data class CurrentUnits(
    @SerialName("temperature_2m") val temperature: String,
    @SerialName("wind_speed_10m") val windSpeed: String,
    val precipitation: String,
)

@Serializable
data class HourlyData(
    val time: List<String>,
    @SerialName("temperature_2m")            val temperature: List<Double>,
    @SerialName("apparent_temperature")      val apparentTemperature: List<Double>,
    @SerialName("precipitation_probability") val precipProb: List<Int>,
    val precipitation: List<Double>,
    @SerialName("weather_code")              val weatherCode: List<Int>,
    @SerialName("wind_speed_10m")            val windSpeed: List<Double>,
    @SerialName("wind_direction_10m")        val windDirection: List<Int>,
)

@Serializable
data class WeatherResponse(
    val current: CurrentWeather? = null,
    @SerialName("current_units") val currentUnits: CurrentUnits? = null,
    val hourly: HourlyData,
)

