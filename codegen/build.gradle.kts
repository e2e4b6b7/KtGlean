import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    antlr
}

dependencies {
    implementation(project(":codegen-runtime"))

    antlr("org.antlr", "antlr4", "4.9.3")
    implementation("com.squareup", "kotlinpoet", "1.11.0")
    implementation("org.jetbrains.kotlinx", "kotlinx-cli", "0.3.4")
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

tasks.register("fatJar", type = Jar::class) {
    archiveFileName.set("codegen.jar")
    manifest {
        attributes["Main-Class"] = "org.jetbrains.research.ktglean.angle.MainKt"
    }
    with(tasks["jar"] as CopySpec)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
