package org.jetbrains.research.ktglean.storage

import org.jetbrains.research.ktglean.serialization.Fact
import java.nio.file.*
import kotlin.io.path.*


class LoggingStorage(path: Path) : FactsStorage {
    private val filePath = path.resolve("log.txt")

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

    override fun addFact(fact: Fact) = log(fact.toString())

    override fun dispose() = log("Disposed")
}
