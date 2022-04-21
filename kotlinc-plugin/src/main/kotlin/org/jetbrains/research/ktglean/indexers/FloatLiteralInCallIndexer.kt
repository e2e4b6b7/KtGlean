package org.jetbrains.research.ktglean.indexers

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.expressions.*
import org.jetbrains.research.ktglean.factories.GleanFloatLiteralUsageFactory
import org.jetbrains.research.ktglean.indexers.base.FirFunctionCallIndexer
import org.jetbrains.research.ktglean.storage.FactsStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FloatLiteralInCallIndexer(private val storage: FactsStorage) : FirFunctionCallIndexer(), KoinComponent {
    private val builder: GleanFloatLiteralUsageFactory by inject()

    override fun index(call: FirFunctionCall, context: CheckerContext) {
        call.arguments.forEachIndexed { i, arg ->
            if (arg is FirConstExpression<*> && arg.value is Float) {
                @Suppress("UNCHECKED_CAST")
                arg as FirConstExpression<Float>
                storage.addFact(builder.getFloatLiteralUsage(call, arg, i, context))
            }
        }
    }
}
