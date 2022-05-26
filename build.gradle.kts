import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20-dev-1435" apply false
    id("com.github.gmazzo.buildconfig") version "3.0.3" apply false
}

allprojects {
    group = "org.jetbrains.research.ktglean"
    version = "0.1.0"

    repositories {
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap") }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            languageVersion = "1.7"
            apiVersion = "1.7"
        }
    }
}

subprojects {
    apply {
        plugin("maven-publish")
    }
}
