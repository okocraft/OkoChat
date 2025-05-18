jcommon {
    setupPaperRepository()
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(projects.okochatIntegration)

    compileOnly(libs.platform.paper)
    compileOnly(libs.integration.vault)
}
