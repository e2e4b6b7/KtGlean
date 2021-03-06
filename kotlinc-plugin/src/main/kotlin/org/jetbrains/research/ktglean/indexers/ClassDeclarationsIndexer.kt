package org.jetbrains.research.ktglean.indexers

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirRegularClass
import org.jetbrains.research.ktglean.factories.GleanClassFactory
import org.jetbrains.research.ktglean.indexers.base.FirRegularClassIndexer
import org.jetbrains.research.ktglean.storage.FactsStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ClassDeclarationsIndexer(private val storage: FactsStorage) : FirRegularClassIndexer(), KoinComponent {
    private val builder: GleanClassFactory by inject()

    override fun index(declaration: FirRegularClass, context: CheckerContext) {
        storage.addFact(builder.getClassDeclaration(declaration, context))
    }
}
