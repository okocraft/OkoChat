plugins {
    id("okochat.common-conventions")
    id("okochat.paper-dependency")
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(libs.integration.placeholderapi)
}
