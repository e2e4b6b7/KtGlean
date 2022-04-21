package org.jetbrains.research.ktglean.indexers

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.utils.classId
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.research.ktglean.factories.*
import org.jetbrains.research.ktglean.indexers.base.FirFunctionCallIndexer
import org.jetbrains.research.ktglean.storage.FactsStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RangeCallIndexer(private val storage: FactsStorage) : FirFunctionCallIndexer(), KoinComponent {
    private val factory: GleanRangeCallFactory by inject()

    override fun index(call: FirFunctionCall, context: CheckerContext) {
        val func = call.callee ?: return
        if (isRange(func.returnTypeRef, context)) {
            storage.addFact(factory.getRangeCall(call, context))
        }
    }

    private fun isRange(typeRef: FirTypeRef, context: CheckerContext): Boolean {
        val firRegularClass = typeRef.firRegularClass(context) ?: return false
        return firRegularClass.classId.asFqNameString() == "kotlin.ranges.ClosedRange"
            || firRegularClass.superTypeRefs.any { isRange(it, context) }
    }
}
