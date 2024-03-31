plugins {
    id("okochat.common-conventions")
}

dependencies {
    compileOnly(libs.gson)
    // implementation(libs.snakeyaml) // For MessageParser#main
}
