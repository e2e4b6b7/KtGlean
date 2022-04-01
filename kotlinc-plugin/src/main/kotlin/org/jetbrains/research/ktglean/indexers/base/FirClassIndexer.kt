package org.jetbrains.research.ktglean.indexers.base

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirClassChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.declarations.FirClass
import java.util.*

abstract class FirClassIndexer : FirClassChecker(), Indexer {
    abstract fun index(declaration: FirClass, context: CheckerContext)

    final override fun check(declaration: FirClass, context: CheckerContext, reporter: DiagnosticReporter) {
        index(declaration, context)
    }

    final override fun singletonExtension(): (FirSession) -> FirAdditionalCheckersExtension = { session ->
        object : FirAdditionalCheckersExtension(session) {
            override val declarationCheckers = object : DeclarationCheckers() {
                override val classCheckers = Collections.singleton(this@FirClassIndexer)
            }
        }
    }
}
