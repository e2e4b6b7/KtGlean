package org.jetbrains.research.ktglean.storage

import org.jetbrains.kotlin.com.intellij.openapi.Disposable
import org.jetbrains.research.ktglean.serialization.Fact

interface FactsStorage : Disposable {
    fun addFact(fact: Fact)
}
