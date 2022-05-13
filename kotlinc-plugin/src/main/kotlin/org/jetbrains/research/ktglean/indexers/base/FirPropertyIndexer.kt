package org.jetbrains.research.ktglean.indexers.base

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirPropertyChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.declarations.FirProperty
import java.util.*

abstract class FirPropertyIndexer : FirPropertyChecker(), Indexer {
    abstract fun index(declaration: FirProperty, context: CheckerContext)

    final override fun check(declaration: FirProperty, context: CheckerContext, reporter: DiagnosticReporter) {
        index(declaration, context)
    }

    final override fun singletonExtension(): (FirSession) -> FirAdditionalCheckersExtension = { session ->
        object : FirAdditionalCheckersExtension(session) {
            override val declarationCheckers = object : DeclarationCheckers() {
                override val propertyCheckers = Collections.singleton(this@FirPropertyIndexer)
            }
        }
    }
}
