plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("buildsrc.convention.spring")
    id("buildsrc.convention.graalvm")
    application
}

dependencies {
    implementation(project(":utils"))
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.spring.boot)
    implementation(libs.postgres)
    implementation(libs.spring.kafka)
    implementation(libs.spring.cloud.circuitbreaker)
    implementation(libs.spring.cloud.retry.circuitbreaker)
    implementation(libs.resilience4j.bulkhead)

    testImplementation(libs.bundles.test.dependencies)
    testRuntimeOnly(libs.bundles.test.runtime)

    developmentOnly(libs.spring.boot.devtools)
}

application {
    mainClass = "com.github.frederikpietzko.cloudnativespring.order.ApplicationKt"
}
