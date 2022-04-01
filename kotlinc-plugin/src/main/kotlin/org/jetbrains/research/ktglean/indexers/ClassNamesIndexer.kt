package org.jetbrains.research.ktglean.indexers

import org.jetbrains.kotlin.fir.analysis.checkers.PsiSourceNavigator.getRawName
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.research.ktglean.Predicate
import org.jetbrains.research.ktglean.indexers.base.FirClassIndexer
import org.jetbrains.research.ktglean.storage.PredicateStorage

class ClassNamesIndexer(private val storage: PredicateStorage) : FirClassIndexer() {
    override fun index(declaration: FirClass, context: CheckerContext) {
        declaration.getRawName()?.let { storage.addPredicate(ClassName(it)) }
    }

    data class ClassName(val name: String) : Predicate
}
