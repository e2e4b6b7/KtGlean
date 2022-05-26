package org.jetbrains.research.ktglean.indexers

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.expressions.*
import org.jetbrains.kotlin.fir.realPsi
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.research.ktglean.factories.GleanXRefFactory
import org.jetbrains.research.ktglean.indexers.base.FirBasicExpressionIndexer
import org.jetbrains.research.ktglean.serialization.Fact
import org.jetbrains.research.ktglean.storage.FactsStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class XRefExpressionIndexer(private val storage: FactsStorage) : FirBasicExpressionIndexer(), KoinComponent {
    private val factory: GleanXRefFactory by inject()

    override fun index(expression: FirStatement, context: CheckerContext) {
        if (expression.realPsi == null) return
        if (expression is FirSafeCallExpression) {
            index(expression.selector, context)
            return
        }
        when (expression) {
            is FirQualifiedAccess -> factory.getXRefs(expression, context).store()
            is FirExpression -> when (expression) {
                is FirAnnotation -> expression.annotationTypeRef.xRef(context).store()
                is FirClassReferenceExpression -> expression.classTypeRef.xRef(context).store()
                is FirResolvedQualifier -> factory.getXRefs(expression, context).store()
                else -> return
            }
            else -> return
        }
    }

    private fun FirTypeRef.xRef(context: CheckerContext) = factory.getXRefs(this, context)
    private fun Iterable<Fact>.store() = forEach { storage.addFact(it) }
}
