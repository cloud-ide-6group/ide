plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(libs.coroutines)
            implementation(libs.koin.core)
            implementation(projects.core.common)
        }
    }
}