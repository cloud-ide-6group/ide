plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.coroutines)
            implementation(projects.core.datastore)
        }

        jvmMain.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.mockk)
            implementation(libs.cash.turbine.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}