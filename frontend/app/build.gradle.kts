import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.buildkonfig)
    kotlin("plugin.serialization") version "2.3.0"
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
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewModel)
            implementation(libs.jetbrains.navigation.compose)

            implementation(projects.feature.authorization)
            implementation(projects.feature.profile)
            implementation(projects.feature.notifications)
            implementation(projects.feature.project)
            implementation(projects.core.designsystem)
            implementation(projects.core.network)
            implementation(projects.core.data)
            implementation(projects.core.common)
            implementation(projects.core.domain)
            implementation(projects.core.navigation)
            implementation(projects.core.datastore)
            implementation(projects.core.auth)
            implementation(projects.core.settings)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "ru.vsu.front.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Code Together"
            packageVersion = "1.0.0"

            windows {
                iconFile.set(project.file("src/jvmMain/resources/app_icon.ico"))
                menuGroup = "Code Together"
                shortcut = true
                dirChooser = true
            }
        }

        buildTypes {
            release {
                proguard {
                    configurationFiles.from(project.file("proguard-rules.pro"))
                }
            }
        }
    }
}

val localProperties = Properties()
val localPropertiesFile = project.rootProject.file("local.properties") ?: error("Не найден файл local.properties")

localProperties.load(FileInputStream(localPropertiesFile))

val serverUrl = localProperties.getProperty("BASE_URL") ?: error("BASE_URL не добавлен в local.properties")

buildkonfig {
    packageName = "ru.vsu.front.config"

    defaultConfigs {
        buildConfigField(STRING, "BASE_URL", serverUrl)
    }
}