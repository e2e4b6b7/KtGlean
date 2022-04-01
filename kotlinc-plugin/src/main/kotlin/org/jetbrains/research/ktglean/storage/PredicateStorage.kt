package org.jetbrains.research.ktglean.storage

import org.jetbrains.kotlin.com.intellij.openapi.Disposable
import org.jetbrains.research.ktglean.Predicate

interface PredicateStorage : Disposable {
    fun addPredicate(predicate: Predicate)
}
