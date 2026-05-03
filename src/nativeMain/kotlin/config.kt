package weathercli

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen
import platform.posix.fputs
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
private val CONFIG_DIR  get() = "${getenv("HOME")?.toKString() ?: getenv("USERPROFILE")?.toKString() ?: "~"}/.config/weather-cli"
private val CONFIG_FILE get() = "$CONFIG_DIR/config"

/** Reads the saved default city, or null if none is set. */
@OptIn(ExperimentalForeignApi::class)
fun readDefaultCity(): String? {
    val f = fopen(CONFIG_FILE, "r") ?: return null
    val line = memScoped {
        val buf = allocArray<kotlinx.cinterop.ByteVar>(256)
        fgets(buf, 256, f)?.toKString()?.trim()
    }
    fclose(f)
    if (line.isNullOrEmpty()) return null
    return if (line.startsWith("default_city=")) line.removePrefix("default_city=").trim() else null
}

/** Saves [city] as the default city config. */
@OptIn(ExperimentalForeignApi::class)
fun saveDefaultCity(city: String) {
    makeDir(CONFIG_DIR)
    val f = fopen(CONFIG_FILE, "w") ?: error("Could not open config file for writing: $CONFIG_FILE")
    fputs("default_city=$city\n", f)
    fclose(f)
}
