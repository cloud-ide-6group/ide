rootProject.name = "front"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":app")
include(":feature:authorization")
include(":feature:profile")
include(":feature:notifications")
include(":feature:project")
include(":core:designsystem")
include(":core:common")
include(":core:domain")
include(":core:data")
include(":core:model")
include(":core:network")
include(":core:navigation")
include(":core:datastore")
include(":core:auth")
include("core:settings")