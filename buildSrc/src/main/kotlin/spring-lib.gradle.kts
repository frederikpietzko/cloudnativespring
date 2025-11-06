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