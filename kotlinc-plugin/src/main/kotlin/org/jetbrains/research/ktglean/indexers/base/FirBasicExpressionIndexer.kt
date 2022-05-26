package org.jetbrains.research.ktglean.indexers.base

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.ExpressionCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirBasicExpressionChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.expressions.FirStatement
import java.util.*

abstract class FirBasicExpressionIndexer : FirBasicExpressionChecker(), Indexer {
    abstract fun index(expression: FirStatement, context: CheckerContext)

    final override fun check(expression: FirStatement, context: CheckerContext, reporter: DiagnosticReporter) {
        index(expression, context)
    }

    final override fun singletonExtension(): (FirSession) -> FirAdditionalCheckersExtension = { session ->
        object : FirAdditionalCheckersExtension(session) {
            override val expressionCheckers = object : ExpressionCheckers() {
                override val basicExpressionCheckers = Collections.singleton(this@FirBasicExpressionIndexer)
            }
        }
    }
}
