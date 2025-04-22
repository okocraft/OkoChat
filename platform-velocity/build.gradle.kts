plugins {
    id("okochat.common-conventions")
    id("okochat.paper-dependency")
    id("okochat.bundle-conventions")
}

project.extra["okochat.plugin-name"] = "OkoChat-Velocity"

dependencies {
    implementation(projects.okochatApi)

    compileOnly(libs.platform.velocity)
}

tasks {
    processResources {
        filesMatching(listOf("velocity-plugin.json")) {
            expand("projectVersion" to project.version)
        }
    }
}
