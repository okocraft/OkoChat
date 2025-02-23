plugins {
    id("okochat.common-conventions")
    id("okochat.paper-dependency")
    id("okochat.bundle-conventions")
}

project.extra["okochat.plugin-name"] = "OkoChat-Legacy"

dependencies {
    implementation(projects.okochatIntegration)
    implementation(libs.adventure.text.serializer.bungeecord) {
        exclude("net.kyori", "adventure-api")
        exclude("net.kyori", "adventure-text-serializer-gson")
        exclude("net.kyori", "adventure-text-serializer-legacy")
    }
    implementation(libs.bungeecord.chat) {
        exclude("com.google.code.gson", "gson")
        exclude("com.google.guava", "guava")
    }
    compileOnly(libs.platform.velocity)
    compileOnly(libs.integration.luckperms)
}

tasks {
    processResources {
        filesMatching(listOf("velocity-plugin.json")) {
            expand("projectVersion" to project.version)
        }
    }
}
