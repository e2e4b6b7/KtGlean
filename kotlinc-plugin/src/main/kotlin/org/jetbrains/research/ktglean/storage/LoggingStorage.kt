package org.jetbrains.research.ktglean.storage

import org.jetbrains.research.ktglean.Predicate
import org.jetbrains.research.ktglean.replaceExtension
import java.nio.file.*
import kotlin.io.path.*

class LoggingStorage(path: Path) : PredicateStorage {
    private val filePath = path.replaceExtension("log")

    init {
        filePath.parent.createDirectories()
        filePath.deleteIfExists()
        filePath.createFile()
    }

    private fun log(str: String) {
        Files.writeString(
            filePath,
            "$str\n",
            StandardOpenOption.APPEND
        )
    }

    override fun addPredicate(predicate: Predicate) = log(predicate.toString())

    override fun dispose() = log("Disposed")
}
