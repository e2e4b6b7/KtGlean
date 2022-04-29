pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap") }
    }
}

rootProject.name = "KtGlean"
include("core")
include("gradle-plugin")
include("kotlinc-plugin")
include("codegen")
include("codegen-runtime")
