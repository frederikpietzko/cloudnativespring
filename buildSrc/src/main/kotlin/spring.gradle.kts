package buildsrc.convention

import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.graalvm.buildtools.native")
}

tasks.withType<BootBuildImage> {
    imageName = "frederikpietzko/${rootProject.name}-${project.name}:latest"
}

allOpen {
    annotations(
        "javax.persistence.Entity",
        "javax.persistence.MappedSuperclass",
        "javax.persistence.Embeddable"
    )
}