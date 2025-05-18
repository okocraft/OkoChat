plugins {
    alias(libs.plugins.bundler)
}

jcommon {
    setupPaperRepository()
}

dependencies {
    implementation(projects.okochatApi)

    compileOnly(libs.platform.velocity)
}

bundler {
    copyToRootBuildDirectory("OkoChat-Velocity-${project.version}")
    replacePluginVersionForVelocity(project.version)
}
