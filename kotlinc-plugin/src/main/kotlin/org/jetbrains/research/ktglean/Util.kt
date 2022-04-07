package org.jetbrains.research.ktglean

import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.name

fun Path.addFreeIndex(): Path {
    var i = 0
    var iPath: Path
    do {
        iPath = resolveSibling("${name}-${i++}")
    } while (iPath.exists())
    return iPath
}
