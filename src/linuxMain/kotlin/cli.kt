package weathercli

import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.fprintf
import platform.posix.stderr

private const val MAX_DAYS = 6

/** Parsed command-line arguments. */
data class CliArgs(
    val dayOffset: Int,
    val cityQuery: String?,
)

fun printHelp() = println(
    """
    Usage: weather [DAY] [--city <name>]

    Displays the weather forecast for a city (default: Bergen, Norway).

      DAY              Days ahead to forecast (0 = today, 1 = tomorrow, up to $MAX_DAYS).
                       Defaults to 0.
      --city <name>    City to show weather for (e.g. "Oslo", "Tokyo", "New York").

    Options:
      -h, --help       Show this help message and exit.

    Examples:
      weather                           # today's forecast for Bergen, Norway
      weather 1                         # tomorrow for Bergen, Norway
      weather --city Oslo               # today for Oslo
      weather 2 --city "New York"       # 2 days ahead for New York
    """.trimIndent()
)

@OptIn(ExperimentalForeignApi::class)
private fun err(msg: String) = fprintf(stderr, "%s\n", msg)

/**
 * Parses [args] into a [CliArgs] instance.
 * Returns `null` if parsing fails or the user requested help.
 */
fun parseArgs(args: Array<String>): CliArgs? {
    if (args.isEmpty()) return CliArgs(0, null)

    var dayOffset: Int? = null
    var city: String?  = null
    var i = 0

    while (i < args.size) {
        when (val token = args[i]) {
            "-h", "--help" -> {
                printHelp()
                return null
            }
            "--city" -> {
                city = args.getOrNull(++i) ?: run {
                    err("Error: --city requires a value.")
                    return null
                }
            }
            else -> {
                if (dayOffset != null) {
                    err("Error: unexpected argument '$token'. Run 'weather --help' for usage.")
                    return null
                }
                val d = token.toIntOrNull() ?: run {
                    err("Error: '$token' is not a number. Run 'weather --help' for usage.")
                    return null
                }
                if (d !in 0..MAX_DAYS) {
                    err("Error: day must be between 0 and $MAX_DAYS.")
                    return null
                }
                dayOffset = d
            }
        }
        i++
    }

    return CliArgs(dayOffset ?: 0, city)
}
