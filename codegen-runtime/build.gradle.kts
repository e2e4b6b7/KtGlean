plugins {
    kotlin("jvm")
}

dependencies {
    implementation("com.google.code.gson", "gson", "2.9.0")
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
        }
    }
}
