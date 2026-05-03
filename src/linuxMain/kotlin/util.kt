package weathercli

/** to a human-readable description.
 * See: https://open-meteo.com/en/docs#weathervariables
 */
fun weatherDescription(code: Int): String = when (code) {
    0        -> "Clear Sky"
    1        -> "Mainly Clear"
    2        -> "Partly Cloudy"
    3        -> "Overcast"
    45, 48   -> "Foggy"
    51       -> "Light Drizzle"
    53       -> "Moderate Drizzle"
    55       -> "Dense Drizzle"
    56, 57   -> "Freezing Drizzle"
    61       -> "Slight Rain"
    63       -> "Moderate Rain"
    65       -> "Heavy Rain"
    66, 67   -> "Freezing Rain"
    71       -> "Slight Snowfall"
    73       -> "Moderate Snowfall"
    75       -> "Heavy Snowfall"
    77       -> "Snow Grains"
    80       -> "Slight Showers"
    81       -> "Moderate Showers"
    82       -> "Violent Showers"
    85, 86   -> "Snow Showers"
    95       -> "Thunderstorm"
    96, 99   -> "Thunderstorm w/ Hail"
    else     -> "Unknown ($code)"
}

/** Returns an emoji icon for the given WMO weather code. */
fun weatherIcon(code: Int): String = when (code) {
    0          -> "☀️"
    1, 2       -> "🌤️"
    3          -> "☁️"
    45, 48     -> "🌫️"
    in 51..57  -> "🌦️"
    in 61..67  -> "🌧️"
    80, 81, 82 -> "🌧️"
    in 71..77  -> "❄️"
    85, 86     -> "🌨️"
    95, 96, 99 -> "⛈️"
    else       -> "🌡️"
}

/** Converts a wind bearing in degrees to a 16-point cardinal direction. */
fun bearingToCardinal(degrees: Int): String {
    val dirs = arrayOf(
        "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
        "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW",
    )
    return dirs[((degrees + 11.25) / 22.5).toInt() % 16]
}

/**
 * Formats a [Double] to one decimal place without using [String.format],
 * which is unavailable in Kotlin/Native.
 */
fun Double.oneDecimal(): String {
    val sign = if (this < 0) "-" else ""
    val abs  = kotlin.math.abs(this)
    val int  = abs.toLong()
    val dec  = ((abs - int) * 10).toLong()
    return "$sign$int.$dec"
}

