plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":codegen-runtime"))

    compileOnly(kotlin("compiler-embeddable"))

    kapt("com.google.auto.service", "auto-service", "1.0.1")
    compileOnly("com.google.auto.service", "auto-service-annotations", "1.0.1")
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
        }
    }
}
