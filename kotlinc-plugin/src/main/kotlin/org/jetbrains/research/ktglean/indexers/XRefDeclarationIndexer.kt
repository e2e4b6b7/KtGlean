package org.jetbrains.research.ktglean.indexers

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.realPsi
import org.jetbrains.kotlin.fir.types.*
import org.jetbrains.research.ktglean.factories.GleanXRefFactory
import org.jetbrains.research.ktglean.indexers.base.FirBasicDeclarationIndexer
import org.jetbrains.research.ktglean.serialization.Fact
import org.jetbrains.research.ktglean.storage.FactsStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class XRefDeclarationIndexer(private val storage: FactsStorage) : FirBasicDeclarationIndexer(), KoinComponent {
    private val factory: GleanXRefFactory by inject()

    override fun index(declaration: FirDeclaration, context: CheckerContext) {
        if (declaration.realPsi == null) return
        when (declaration) {
            is FirConstructor -> return
            is FirCallableDeclaration -> {
                if (!declaration.returnTypeRef.isUnit) declaration.returnTypeRef.xRefs(context).store()
                declaration.receiverTypeRef?.xRefs(context)?.store()
            }
            is FirClass -> declaration.superTypeRefs.forEach {
                if (!it.isAny) it.xRefs(context).store()
            }
            is FirTypeAlias -> declaration.expandedTypeRef.xRefs(context).store()
            is FirTypeParameter -> declaration.bounds.forEach { it.xRefs(context).store() }
            else -> return
        }
    }

    private fun FirTypeRef.xRefs(context: CheckerContext) = factory.getXRefs(this, context)
    private fun Iterable<Fact>.store() = forEach { storage.addFact(it) }
}
