package org.jetbrains.research.ktglean.storage

import org.jetbrains.research.ktglean.serialization.Fact
import org.jetbrains.research.ktglean.serialization.PredicateFacts
import java.nio.file.Path
import kotlin.io.path.*


class JsonStorage(path: Path) : FactsStorage {
    private val filePath = path.resolve("data.json")
    private val facts = HashMap<String, PredicateFacts>()

    init {
        filePath.parent.createDirectories()
        filePath.deleteIfExists()
        filePath.createFile()
    }

    override fun addFact(fact: Fact) {
        facts.computeIfAbsent(fact.name, ::PredicateFacts).facts.add(fact)
    }

    override fun dispose() {
        filePath.bufferedWriter().use {
            PredicateFacts.writeBatch(facts.values, it)
        }
    }
}
