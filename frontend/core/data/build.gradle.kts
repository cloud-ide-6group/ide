plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.3.0"
}

group = "ru.vsu"

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.socketio)
    implementation(libs.coroutines)
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.datastore)
}