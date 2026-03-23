plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(libs.coroutines)
            implementation(libs.koin.core)
            implementation(libs.androidx.datastore.preferences)
            implementation(projects.core.common)
        }
    }
}