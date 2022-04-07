package org.jetbrains.research.ktglean.indexers.base

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirRegularClassChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.declarations.FirRegularClass
import java.util.*

abstract class FirRegularClassIndexer : FirRegularClassChecker(), Indexer {
    abstract fun index(declaration: FirRegularClass, context: CheckerContext)

    final override fun check(declaration: FirRegularClass, context: CheckerContext, reporter: DiagnosticReporter) {
        index(declaration, context)
    }

    final override fun singletonExtension(): (FirSession) -> FirAdditionalCheckersExtension = { session ->
        object : FirAdditionalCheckersExtension(session) {
            override val declarationCheckers = object : DeclarationCheckers() {
                override val regularClassCheckers = Collections.singleton(this@FirRegularClassIndexer)
            }
        }
    }
}
