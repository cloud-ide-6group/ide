plugins {
    alias(libs.plugins.kotlinMultiplatform)
    kotlin("plugin.serialization") version "2.3.0"
    alias(libs.plugins.dokka)
}

kotlin {
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(libs.coroutines)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.core.common)
            implementation(projects.core.model)
        }
    }
}