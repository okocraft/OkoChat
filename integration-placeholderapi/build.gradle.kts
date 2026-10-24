jcommon {
    setupPaperRepository()
}

repositories {
    maven("https://repo.extendedclip.com/releases/")
}

dependencies {
    compileOnly(libs.platform.paper)
    compileOnly(libs.integration.placeholderapi)
}
