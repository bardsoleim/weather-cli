# weather-cli

A fast, zero-dependency terminal weather app for Linux, built with **Kotlin Native**.  
No JVM required — runs as a native binary.

Weather data is provided by the free [Open-Meteo](https://open-meteo.com) API.  
No API key needed.

---

## Install

```sh
curl -fsSL https://raw.githubusercontent.com/bardsoleim/weather-cli/main/install.sh | bash
```

The script will:
1. Clone this repo into a temporary directory
2. Build the native binary with Gradle
3. Install it to `~/.local/bin/weather`

**Requirements:** `git`, JDK 21+, `libcurl` (`sudo pacman -S curl jdk21-openjdk` on CachyOS/Arch)

---

## Features

- Live current conditions (temperature, feels-like, humidity, wind, pressure)
- Full 24-hour hourly forecast
- Forecast up to 6 days ahead
- Look up any city in the world

---

## Usage

```
weather [DAY] [--city <name>]

  DAY              Days ahead (0 = today, 1 = tomorrow, … up to 6). Default: 0
  --city <name>    City name to look up (default: Bergen, Norway)

Options:
  -h, --help       Show help and exit
```

### Examples

```sh
weather                           # today's forecast for Bergen, Norway
weather 1                         # tomorrow for Bergen, Norway
weather --city Oslo               # today for Oslo
weather 2 --city "New York"       # 2 days ahead for New York
weather 3 --city Tokyo            # 3 days ahead for Tokyo
```

---

## Building from source

### Requirements

- JDK 21+
- `libcurl` development headers (`sudo pacman -S curl` on CachyOS/Arch)

### Build

```sh
./gradlew linkWeatherReleaseExecutableLinux
```

The binary will be at:

```
build/bin/linux/weatherReleaseExecutable/weather.kexe
```

### Install

```sh
cp build/bin/linux/weatherReleaseExecutable/weather.kexe ~/.local/bin/weather
```

Make sure `~/.local/bin` is on your `$PATH`.  
On fish shell: `fish_add_path ~/.local/bin`

---

## Project structure

```
src/linuxMain/kotlin/
├── main.kt      — entry point, wires everything together
├── cli.kt       — argument parsing and help text
├── api.kt       — geocoding and weather API calls
├── model.kt     — serializable data classes
├── display.kt   — terminal output / formatting
└── util.kt      — weather codes, wind direction, number formatting
```

---

## License

MIT — see [LICENSE](LICENSE)
