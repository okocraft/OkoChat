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
    "bridge-paper",
    "bridge-protocol",
    "integration",
    "integration-luckperms",
    "integration-placeholderapi",
    "integration-vault",

    "legacy"
).forEach {
    include("$okochatPrefix-$it")
    project(":$okochatPrefix-$it").projectDir = file(it)
}
