plugins {
    `java-library`

    id("okochat.publication")
}

repositories {
    mavenCentral()
}

val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

dependencies {
    implementation(libs.annotations)
    implementation(libs.configapi.format.yaml) {
        exclude("org.yaml", "snakeyaml")
    }
    implementation(libs.event4j)

    compileOnlyApi(libs.adventure)
    compileOnlyApi(libs.adventure.text.minimessage)
    compileOnlyApi(libs.adventure.text.serializer.legacy)
    compileOnlyApi(libs.adventure.text.serializer.plain)
    compileOnlyApi(libs.slf4j)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.adventure)
    testImplementation(libs.adventure.text.minimessage)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly(libs.slf4j.simple)
    testRuntimeOnly(libs.snakeyaml)
}

val javaVersion = JavaVersion.VERSION_21
val charset = Charsets.UTF_8

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks {
    compileJava {
        options.encoding = charset.name()
        options.release.set(javaVersion.ordinal + 1)
        options.compilerArgs.addAll(sequenceOf(
                "-Xmaxerrs", "9999",
                "-Xmaxwarns", "9999"
        ))
    }

    processResources {
        filteringCharset = charset.name()
    }

    test {
        useJUnitPlatform()

        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
