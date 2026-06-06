plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.dokka)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            api(projects.core.model)
            implementation(libs.coroutines)
            implementation(libs.socketio)
        }

        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.mockk)
        }
    }
}