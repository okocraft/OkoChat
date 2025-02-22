plugins {
    id("okochat.common-conventions")
    id("okochat.paper-dependency")
    id("okochat.bundle-conventions")
}

project.extra["okochat.plugin-name"] = "OkoChatBridge-Paper"

dependencies {
    implementation(projects.okochatBridgeProtocol)
    implementation(projects.okochatIntegration)
    implementation(projects.okochatIntegrationLuckperms)
    implementation(projects.okochatIntegrationVault)
}

tasks {
    processResources {
        filesMatching(listOf("paper-plugin.yml")) {
            expand("projectVersion" to project.version)
        }
    }
}
