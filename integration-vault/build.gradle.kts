plugins {
    id("okochat.common-conventions")
    id("okochat.paper-dependency")
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(projects.okochatIntegration)
    compileOnly(libs.integration.vault)
}
