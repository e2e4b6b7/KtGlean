package org.jetbrains.research.ktglean

import org.gradle.api.Project

open class KtGleanGradleExtension(target: Project) {
    var outputDirectory: String = target.rootDir.resolve("KtGleanData").absolutePath
}
