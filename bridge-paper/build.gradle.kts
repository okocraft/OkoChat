plugins {
    alias(libs.plugins.bundler)
    alias(libs.plugins.run.paper)
}

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

bundler {
    copyToRootBuildDirectory("OkoChatBridge-Paper-${project.version}")
    replacePluginVersionForPaper(project.version)
}

tasks {
    runServer {
        minecraftVersion(libs.versions.paper.get().removeSuffix("-R0.1-SNAPSHOT"))
    }
}
