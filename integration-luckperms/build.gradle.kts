plugins {
    id("okochat.common-conventions")
}

dependencies {
    implementation(projects.okochatIntegration)
    compileOnly(libs.integration.luckperms)
}
