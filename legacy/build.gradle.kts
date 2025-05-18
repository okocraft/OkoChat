plugins {
    id("okochat.bundle-conventions")
}

project.extra["okochat.plugin-name"] = "OkoChat-Legacy"

jcommon {
    setupPaperRepository()
}

dependencies {
    implementation(projects.okochatApi)
    implementation(projects.okochatBridgeProtocol)
    implementation(projects.okochatIntegration)
    implementation(projects.okochatIntegrationLuckperms)
    implementation(projects.okochatPlatformVelocity)
    implementation(libs.adventure.text.serializer.bungeecord) {
        exclude("net.kyori", "adventure-api")
        exclude("net.kyori", "adventure-text-serializer-gson")
        exclude("net.kyori", "adventure-text-serializer-legacy")
    }
    implementation(libs.bungeecord.chat) {
        exclude("com.google.code.gson", "gson")
        exclude("com.google.guava", "guava")
    }
    compileOnly(libs.platform.velocity)
}

tasks {
    processResources {
        filesMatching(listOf("velocity-plugin.json")) {
            expand("projectVersion" to project.version)
        }
    }
}
