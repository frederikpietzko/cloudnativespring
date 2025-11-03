plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("buildsrc.convention.spring")
}

dependencies {
    // Apply the kotlinx bundle of dependencies from the version catalog (`gradle/libs.versions.toml`).
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.spring.boot)
    testImplementation(kotlin("test"))
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}