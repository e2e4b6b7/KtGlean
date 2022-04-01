package org.jetbrains.research.ktglean.storage

import org.jetbrains.research.ktglean.replaceExtension
import org.jetbrains.research.ktglean.serialization.Fact
import org.jetbrains.research.ktglean.serialization.PredicateFacts
import java.nio.file.Path
import kotlin.io.path.*


class JsonStorage(path: Path) : FactsStorage {
    private val filePath = path.replaceExtension("json")
    private val predicates = HashMap<String, PredicateFacts>()

    init {
        filePath.parent.createDirectories()
        filePath.deleteIfExists()
        filePath.createFile()
    }

    override fun addFact(fact: Fact) {
        predicates.computeIfAbsent(fact.name(), ::PredicateFacts).facts.add(fact)
    }

    override fun dispose() {
        filePath.bufferedWriter().use {
            PredicateFacts.writeBatch(predicates.values, it)
        }
    }
}
