package org.jetbrains.research.ktglean.indexers

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirRegularClass
import org.jetbrains.research.ktglean.builders.GleanClassBuilder
import org.jetbrains.research.ktglean.indexers.base.FirRegularClassIndexer
import org.jetbrains.research.ktglean.storage.FactsStorage
import org.koin.core.component.inject

class ClassDeclarationsIndexer(private val storage: FactsStorage) : FirRegularClassIndexer() {
    private val builder: GleanClassBuilder by inject()

    override fun index(declaration: FirRegularClass, context: CheckerContext) {
        storage.addFact(builder.getClass(declaration, context))
    }
}
