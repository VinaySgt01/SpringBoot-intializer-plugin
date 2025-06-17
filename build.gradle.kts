import org.jetbrains.intellij.tasks.RunIdeTask

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("org.jetbrains.intellij") version "1.17.3"
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

intellij {
    version.set("2024.1")
    type.set("IC")
    plugins.set(listOf(
        "java","gradle"
    ))
}


tasks {
    // ✅ Java compile settings
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.compilerArgs.addAll(listOf("--release", "17"))
        modularity.inferModulePath.set(false)
    }

    // ✅ Kotlin compile settings
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("251.*")
    }

    buildSearchableOptions {
        enabled = false
    }

    // ✅ This is where your RunIdeTask config goes — not nested inside another `tasks` block
    withType<RunIdeTask> {
//        ideDir.set(file("C:/Program Files/JetBrains/IntelliJ IDEA Community Edition 2025.1.1.1"))
        autoReloadPlugins.set(true)
    }
}
