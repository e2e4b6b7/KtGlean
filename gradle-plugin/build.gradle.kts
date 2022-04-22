plugins {
    kotlin("jvm")
    id("com.github.gmazzo.buildconfig")
    `java-gradle-plugin`
}

dependencies {
    implementation(project(":core"))

    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin-api"))
}

buildConfig {
    val project = project(":kotlinc-plugin")
    packageName(group.toString())
    buildConfigField("String", "PLUGIN_GROUP_ID", "\"${project.group}\"")
    buildConfigField("String", "PLUGIN_ARTIFACT_ID", "\"${project.name}\"")
    buildConfigField("String", "PLUGIN_VERSION", "\"${project.version}\"")
}

gradlePlugin {
    plugins {
        create("KtGlean") {
            id = "org.jetbrains.research.ktglean"
            displayName = "Kotlin Glean Plugin"
            description = "Kotlin Compiler Plugin for Glean indexing"
            implementationClass = "org.jetbrains.research.ktglean.KtGleanGradlePlugin"
        }
    }
}
