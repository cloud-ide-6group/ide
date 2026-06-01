// build.gradle.kts (внутри модуля navigation)
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    kotlin("plugin.serialization") version "2.3.0"
    alias(libs.plugins.dokka)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
    }
}