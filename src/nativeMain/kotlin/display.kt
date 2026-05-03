package weathercli

private const val BOX_WIDTH = 58  // inner width of the header box

/** Prints the top-of-output header box. */
fun printHeader(cityName: String, label: String) {
    val inner = "  🌦  ${cityName.take(30).padEnd(30)}  ${label.padEnd(10)}"
    println("╔${"═".repeat(BOX_WIDTH)}╗")
    println("║$inner║")
    println("╚${"═".repeat(BOX_WIDTH)}╝")
    println()
}

/** Prints the live "NOW" current-conditions block (today only). */
fun printCurrentConditions(current: CurrentWeather, units: CurrentUnits) {
    val icon      = weatherIcon(current.weatherCode)
    val condition = weatherDescription(current.weatherCode)
    val cardinal  = bearingToCardinal(current.windDirection)

    println("  NOW  $icon  $condition")
    println("  ${"━".repeat(52)}")
    println("  🌡️  Temp      : ${current.temperature}${units.temperature}  (feels like ${current.apparentTemperature}${units.temperature})")
    println("  💧  Humidity  : ${current.humidity}%")
    println("  🌬️  Wind      : ${current.windSpeed} ${units.windSpeed} from $cardinal (${current.windDirection}°)")
    println("  🌧️  Precip    : ${current.precipitation} ${units.precipitation}")
    println("  📊  Pressure  : ${current.pressure} hPa")
    println("  ${"━".repeat(52)}")
    println()
}

/** Prints the 24-hour hourly forecast table for [targetDate] (YYYY-MM-DD). */
fun printHourlyForecast(hourly: HourlyData, targetDate: String) {
    val indices = hourly.time.indices.filter { hourly.time[it].startsWith(targetDate) }

    println("  HOURLY FORECAST — $targetDate")
    println(
        "  ${"Time".padEnd(5)}  ${"".padEnd(2)}  ${"Condition".padEnd(22)}" +
        "  ${"Temp".padStart(7)}  ${"Feels".padStart(7)}  ${"Rain%".padStart(5)}" +
        "  ${"Precip".padStart(7)}  Wind"
    )
    println("  ${"─".repeat(74)}")

    for (i in indices) {
        val time  = hourly.time[i].substring(11, 16)
        val icon  = weatherIcon(hourly.weatherCode[i])
        val cond  = weatherDescription(hourly.weatherCode[i]).take(22).padEnd(22)
        val temp  = "${hourly.temperature[i].oneDecimal()}°C".padStart(7)
        val feels = "${hourly.apparentTemperature[i].oneDecimal()}°C".padStart(7)
        val prob  = "${hourly.precipProb[i]}%".padStart(5)
        val prec  = "${hourly.precipitation[i].oneDecimal()}mm".padStart(7)
        val wind  = "${hourly.windSpeed[i]}m/s ${bearingToCardinal(hourly.windDirection[i])}"
        println("  $time  $icon  $cond  $temp  $feels  $prob  $prec  $wind")
    }

    println("  ${"─".repeat(74)}")
    println()
    println("  Source: Open-Meteo (https://open-meteo.com)")
}

