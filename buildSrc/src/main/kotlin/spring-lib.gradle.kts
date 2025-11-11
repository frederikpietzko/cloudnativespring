package buildsrc.convention

import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

allOpen {
    annotations(
        "javax.persistence.Entity",
        "javax.persistence.MappedSuperclass",
        "javax.persistence.Embeddable"
    )
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.withType<Jar> {
    enabled = true
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.1.0-M4")
        mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.21.0")
    }
}
