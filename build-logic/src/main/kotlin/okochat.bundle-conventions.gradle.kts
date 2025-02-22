plugins {
    `java-library`
    id("com.gradleup.shadow")
}

project.extra["okochat.plugin-name"] = "unknown"

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
    return rootProject.layout.buildDirectory.dir("libs").get().file("${project.extra["okochat.plugin-name"]}-${project.version}.jar").asFile
}
