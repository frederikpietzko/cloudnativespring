plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("buildsrc.convention.spring-lib")
}

dependencies {
    // Apply the kotlinx bundle of dependencies from the version catalog (`gradle/libs.versions.toml`).
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.spring.boot)
    implementation(libs.spring.kafka)

    testImplementation(kotlin("test"))
}