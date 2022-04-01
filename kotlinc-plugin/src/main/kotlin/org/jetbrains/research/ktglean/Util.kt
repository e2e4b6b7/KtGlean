package org.jetbrains.research.ktglean

import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

fun Path.replaceExtension(extension: String): Path = resolveSibling("$nameWithoutExtension.$extension")
