package org.jetbrains.research.ktglean.indexers.base

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.ExpressionCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirFunctionCallChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import java.util.*

abstract class FirFunctionCallIndexer : FirFunctionCallChecker(), Indexer {
    abstract fun index(call: FirFunctionCall, context: CheckerContext)

    final override fun check(expression: FirFunctionCall, context: CheckerContext, reporter: DiagnosticReporter) {
        index(expression, context)
    }

    final override fun singletonExtension(): (FirSession) -> FirAdditionalCheckersExtension = { session ->
        object : FirAdditionalCheckersExtension(session) {
            override val expressionCheckers = object : ExpressionCheckers() {
                override val functionCallCheckers = Collections.singleton(this@FirFunctionCallIndexer)
            }
        }
    }
}
