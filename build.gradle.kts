plugins {
    kotlin("multiplatform") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
}

group = "io.github.weathercli"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    // Suppress default hierarchy template warning caused by the custom target name.
    applyDefaultHierarchyTemplate()

    linuxX64("linux") {
        binaries {
            executable("weather") {
                entryPoint = "weathercli.main"
            }
        }
    }

    sourceSets {
        val linuxMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:3.1.3")
                implementation("io.ktor:ktor-client-curl:3.1.3")
                implementation("io.ktor:ktor-client-content-negotiation:3.1.3")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
            }
        }
    }
}