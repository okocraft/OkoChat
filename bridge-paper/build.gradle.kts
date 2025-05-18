plugins {
    id("okochat.bundle-conventions")
}

project.extra["okochat.plugin-name"] = "OkoChatBridge-Paper"

jcommon {
    setupPaperRepository()
}

dependencies {
    compileOnly(libs.platform.paper)

    implementation(projects.okochatBridgeProtocol)
    implementation(projects.okochatIntegration)
    implementation(projects.okochatIntegrationLuckperms)
    implementation(projects.okochatIntegrationPlaceholderapi)
    implementation(projects.okochatIntegrationVault)
}

tasks {
    processResources {
        filesMatching(listOf("paper-plugin.yml")) {
            expand("projectVersion" to project.version)
        }
    }
}
