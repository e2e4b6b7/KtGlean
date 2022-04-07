package org.jetbrains.research.ktglean.indexers.base

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirFunctionChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.declarations.FirFunction
import java.util.*

abstract class FirFunctionIndexer : FirFunctionChecker(), Indexer {
    abstract fun index(declaration: FirFunction, context: CheckerContext)

    final override fun check(declaration: FirFunction, context: CheckerContext, reporter: DiagnosticReporter) {
        index(declaration, context)
    }

    final override fun singletonExtension(): (FirSession) -> FirAdditionalCheckersExtension = { session ->
        object : FirAdditionalCheckersExtension(session) {
            override val declarationCheckers = object : DeclarationCheckers() {
                override val functionCheckers = Collections.singleton(this@FirFunctionIndexer)
            }
        }
    }
}
