jcommon {
    setupPaperRepository()
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(libs.platform.paper)
    compileOnly(libs.integration.placeholderapi)
}
