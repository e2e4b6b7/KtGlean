package org.jetbrains.research.ktglean.indexers

import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.expressions.*
import org.jetbrains.kotlin.fir.psi
import org.jetbrains.research.ktglean.factories.GleanXRefFactory
import org.jetbrains.research.ktglean.indexers.base.FirStatementIndexer
import org.jetbrains.research.ktglean.storage.FactsStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.nio.file.*

fun log(firElement: FirElement, prefix: String) {
    Files.writeString(
        Path.of("/home/roman/KtGlean/logg.txt"),
        "$prefix ${firElement::class.qualifiedName}: ${firElement.psi?.text}\n",
        StandardOpenOption.APPEND,
        StandardOpenOption.CREATE,
    )
}

class XRefIndexer(private val storage: FactsStorage) : FirStatementIndexer(), KoinComponent {
    private val builder: GleanXRefFactory by inject()

    override fun index(expression: FirStatement, context: CheckerContext) {
        when (expression) {
            is FirQualifiedAccess -> builder.getXRef(expression, context)
            is FirExpression -> when (expression) {
                is FirAnnotation -> builder.getXRef(expression.annotationTypeRef, context)
                is FirClassReferenceExpression -> builder.getXRef(expression.classTypeRef, context)
                else -> {
                    log(expression, "i")
                    return
                }
            }
            else -> {
                log(expression, "i")
                return
            }
        }?.let { storage.addFact(it) }
    }
}
