plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.dokka)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.koin.compose.viewModel)
            implementation(libs.koin.core)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.uiToolingPreview)
            implementation(projects.core.domain)
            implementation(projects.core.common)
            implementation(projects.core.model)
            implementation(projects.core.designsystem)
        }
    }
}