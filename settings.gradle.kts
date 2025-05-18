pluginManagement {
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
    "core",

    "bridge-paper",
    "bridge-protocol",

    "integration",
    "integration-luckperms",
    "integration-placeholderapi",
    "integration-vault",

    "platform-velocity",

    "legacy"
).forEach {
    include("$okochatPrefix-$it")
    project(":$okochatPrefix-$it").projectDir = file(it)
}
