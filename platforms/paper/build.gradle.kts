plugins {
    id ("okochat.platform-conventions")
}

project.extra["platform.name"] = "Paper"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(projects.okochatCore)
   // implementation(projects.okochatBridgeApi)
   // implementation(projects.okochatBridgeLuckperms)
  //  implementation(projects.okochatBridgeVault)

    compileOnly(libs.platform.paper)
}

tasks {
    processResources {
        filesMatching(listOf("plugin.yml")) {
            expand("projectVersion" to project.version)
        }
    }
}
