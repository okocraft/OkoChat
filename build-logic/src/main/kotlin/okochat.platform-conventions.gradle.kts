plugins {
    id("okochat.common-conventions")
    id("com.gradleup.shadow")
}

project.extra["platform.name"] = "unknown"

dependencies {
    implementation(project(":okochat-core"))
}

tasks {
    build {
        dependsOn(shadowJar)
        doLast {
            val filepath = getArtifactFilepath()
            filepath.parentFile.mkdirs()
            shadowJar.get().archiveFile.get().asFile.copyTo(filepath, true)
        }
    }

    clean {
        doLast {
            getArtifactFilepath().delete()
        }
    }
}

fun getArtifactFilepath(): File {
    return rootProject.layout.buildDirectory.dir("libs").get().file("OkoChat-${project.extra["platform.name"]}-${project.version}.jar").asFile
}
