import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    antlr
}

dependencies {
    implementation(project(":codegen-runtime"))

    antlr("org.antlr", "antlr4", "4.9.3")
    implementation("com.squareup", "kotlinpoet", "1.11.0")
}

tasks.withType<KotlinCompile> {
    dependsOn("generateGrammarSource")
}

tasks.generateGrammarSource {
    val sourcesPackage = "org.jetbrains.research.ktglean.angle.parser.antlr"
    val generatedDirectory = buildDir.resolve("generated-src/antlr/main")

    sourceSets["main"].java.srcDirs += generatedDirectory

    arguments.add("-no-listener")
    arguments.add("-visitor")
    arguments.add("-package")
    arguments.add(sourcesPackage)
    outputDirectory = generatedDirectory.resolve(sourcesPackage.replace('.', File.separatorChar))
}
