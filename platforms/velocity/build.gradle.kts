plugins {
    id ("okochat.platform-conventions")
}

project.extra["platform.name"] = "Velocity"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.platform.velocity)
}

tasks {
    processResources {
        filesMatching(listOf("velocity-plugin.json")) {
            expand("projectVersion" to project.version)
        }
    }
}
