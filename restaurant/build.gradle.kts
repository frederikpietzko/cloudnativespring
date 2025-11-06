plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("buildsrc.convention.spring")
    application
}

dependencies {
    implementation(project(":utils"))
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.spring.boot)
    implementation(libs.postgres)
    implementation(libs.spring.kafka)

    testImplementation(libs.bundles.test.dependencies)
    testRuntimeOnly(libs.bundles.test.runtime)

    developmentOnly(libs.spring.boot.devtools)
}

application {
    mainClass = "com.github.frederikpietzko.cloudnativespring.restaurant.ApplicationKt"
}
