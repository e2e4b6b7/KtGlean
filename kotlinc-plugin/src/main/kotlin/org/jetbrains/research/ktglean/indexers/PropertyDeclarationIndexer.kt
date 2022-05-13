package org.jetbrains.research.ktglean.indexers

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirProperty
import org.jetbrains.research.ktglean.factories.GleanPropertyFactory
import org.jetbrains.research.ktglean.indexers.base.FirPropertyIndexer
import org.jetbrains.research.ktglean.storage.FactsStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PropertyDeclarationIndexer(private val storage: FactsStorage) : FirPropertyIndexer(), KoinComponent {
    private val builder: GleanPropertyFactory by inject()

    override fun index(declaration: FirProperty, context: CheckerContext) {
        storage.addFact(builder.getProperty(declaration, context))
    }
}
