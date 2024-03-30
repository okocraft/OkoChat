pluginManagement {
    includeBuild("build-logic")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "OkoChat"
val okochatPrefix = rootProject.name.lowercase()

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

sequenceOf(
    "api",
    "core"
).forEach {
    include("$okochatPrefix-$it")
    project(":$okochatPrefix-$it").projectDir = file(it)
}

sequenceOf(
        "paper",
        "velocity"
).forEach {
    include("$okochatPrefix-platform-$it")
    project(":$okochatPrefix-platform-$it").projectDir = file("./platforms/$it")
}
