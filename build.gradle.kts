plugins {
    alias(libs.plugins.jcommon)
    alias(libs.plugins.mavenPublication)
}

val isReleaseVersion = !project.version.toString().endsWith("-SNAPSHOT")

jcommon {
    javaVersion = JavaVersion.VERSION_21

    commonDependencies {
        compileOnlyApi(libs.annotations)
        compileOnlyApi(libs.adventure)
        compileOnlyApi(libs.adventure.text.minimessage)
        compileOnlyApi(libs.adventure.text.serializer.gson)
        compileOnlyApi(libs.adventure.text.serializer.legacy)
        compileOnlyApi(libs.adventure.text.serializer.plain)
        compileOnlyApi(libs.slf4j)

        testImplementation(platform(libs.junit.bom))
        testImplementation(libs.junit.jupiter)
        testImplementation(libs.adventure)
        testImplementation(libs.adventure.text.minimessage)
        testImplementation(libs.adventure.text.serializer.gson)
        testImplementation(libs.adventure.text.serializer.legacy)
        testImplementation(libs.adventure.text.serializer.plain)
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        testRuntimeOnly(libs.slf4j.simple)
    }

    javadocTask {
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

mavenPublication {
    val dirName = if (isReleaseVersion) "maven" else "maven-snapshot"
    localRepository(rootProject.projectDir.resolve("staging").resolve(dirName))
    description("A Chat Plugin for Minecraft.")
    gplV3License()
    developer("Siroshun09")
    github("okocraft/OkoChat")
}
