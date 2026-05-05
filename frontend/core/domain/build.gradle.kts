plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            api(projects.core.model)
            implementation(libs.coroutines)
        }

        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.mockk)
        }
    }
}