import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    `java-library`
    `maven-publish`
}

val libs = extensions.getByType(LibrariesForLibs::class)

java {
    if (isPublishing(project)) {
        withJavadocJar()
        withSourcesJar()
    }
}

tasks {
    javadoc {
        val opts = options as StandardJavadocDocletOptions

        opts.encoding = Charsets.UTF_8.name()
        opts.addStringOption("Xdoclint:none", "-quiet")
        opts.links(
                "https://jd.advntr.dev/api/${libs.versions.adventure.get()}",
                "https://jd.advntr.dev/test-minimessage/${libs.versions.adventure.get()}",
                "https://javadoc.io/doc/org.jetbrains/annotations/${libs.versions.annotations.get()}/"
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])

            pom {
                name.set(project.name)
                description.set("A Chat Plugin for Minecraft")
                url.set("https://github.com/okocraft/OkoChat")

                licenses {
                    license {
                        name.set("GNU General Public License, Version 3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/okocraft/OkoChat.git")
                    developerConnection.set("scm:git:git@github.com:okocraft/OkoChat.git")
                    url.set("https://github.com/okocraft/OkoChat")
                }
            }
        }
    }

    repositories {
        maven {
            val dirName = if (isReleaseVersion(project)) "maven" else "maven-snapshot"
            url = uri(rootDir.resolve("staging").resolve(dirName))
        }
    }
}
