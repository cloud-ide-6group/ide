plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.dokka)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(libs.compose.material3)
            implementation(libs.koin.core)
        }
    }
}