plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.coroutines)
            implementation(projects.core.datastore)
        }

        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.mockk)
            implementation(libs.cash.turbine.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}