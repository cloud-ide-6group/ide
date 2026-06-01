plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.dokka)
}

group = "ru.vsu"

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {

        }
    }
}