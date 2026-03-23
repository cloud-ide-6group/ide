plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "ru.vsu"

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {

        }
    }
}