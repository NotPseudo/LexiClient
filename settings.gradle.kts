pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.polyfrost.cc/releases") // Adds the Polyfrost maven repository to get Polyfrost Gradle Toolkit
    }
    plugins {
        val pgtVersion = "0.1.28" // Sets the default versions for Polyfrost Gradle Toolkit
        id("cc.polyfrost.multi-version.root") version pgtVersion
    }
}

val mod_name: String by settings

// Configures the root project Gradle name based on the value in `gradle.properties`
rootProject.name = mod_name
rootProject.buildFileName = "root.gradle.kts"

// Adds all of our build target versions to the classpath if we need to add version-specific code.
listOf(
    "1.8.9-forge", // Update this if you want to remove/add a version, along with `build.gradle.kts` and `root.gradle.kts`.
    "1.12.2-forge"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}
