plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("buildsrc.convention.spring")
    id("buildsrc.convention.graalvm")
    application
}

dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web.mvc)
    implementation(libs.spring.cloud.config.server)
}

application {
    mainClass = "com.frederikpietzko.cloudnativespring.configserver.ApplicationKt"
}