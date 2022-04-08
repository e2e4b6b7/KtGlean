package org.jetbrains.research.ktglean.indexers

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirSimpleFunction
import org.jetbrains.research.ktglean.builders.GleanFunctionBuilder
import org.jetbrains.research.ktglean.indexers.base.FirSimpleFunctionIndexer
import org.jetbrains.research.ktglean.storage.FactsStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FunctionDeclarationsIndexer(private val storage: FactsStorage) : FirSimpleFunctionIndexer(), KoinComponent {
    private val builder: GleanFunctionBuilder by inject()

    override fun index(declaration: FirSimpleFunction, context: CheckerContext) {
        storage.addFact(builder.getFunction(declaration, context))
    }
}
