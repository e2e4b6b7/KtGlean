package org.jetbrains.research.ktglean

import java.nio.file.Path
import kotlin.io.path.*

fun Path.replaceExtension(extension: String): Path = resolveSibling("$nameWithoutExtension.$extension")

fun Path.addFreeIndex(): Path {
    var i = 0
    var iPath: Path
    do {
        iPath = resolveSibling("${name}-${i++}")
    } while (iPath.exists())
    return iPath
}
