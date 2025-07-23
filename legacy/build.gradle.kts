plugins {
    alias(libs.plugins.bundler)
    alias(libs.plugins.run.velocity)
}

jcommon {
    setupPaperRepository()
}

dependencies {
    implementation(projects.okochatApi)
    implementation(projects.okochatCore)
    implementation(projects.okochatBridgeProtocol)
    implementation(projects.okochatIntegration)
    implementation(projects.okochatIntegrationLuckperms)
    implementation(projects.okochatPlatformVelocity)
    compileOnly(libs.platform.velocity)
    // implementation(libs.snakeyaml) // For MessageParser#main
}

bundler {
    copyToRootBuildDirectory("OkoChat-Legacy-${project.version}")
    replacePluginVersionForVelocity(project.version)
}

tasks {
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}
