dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":utils")
include(":configserver")
include(":gateway")
include(":restaurant")
include(":customer")
include(":order")

rootProject.name = "cloudnativespring"