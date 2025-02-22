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
    "bridge-protocol",
).forEach {
    include("$okochatPrefix-$it")
    project(":$okochatPrefix-$it").projectDir = file(it)
}
