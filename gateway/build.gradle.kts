plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("buildsrc.convention.spring")
    id("buildsrc.convention.graalvm")
    application
}

dependencies {
    implementation(libs.bundles.gateway)

    testImplementation(libs.bundles.test.dependencies)
    testRuntimeOnly(libs.bundles.test.runtime)

    developmentOnly(libs.spring.boot.devtools)
}

application {
    mainClass = "com.github.frederikpietzko.cloudnativespring.gateway.ApplicationKt"
}
