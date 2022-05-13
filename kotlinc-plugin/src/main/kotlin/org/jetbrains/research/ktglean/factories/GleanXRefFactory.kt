package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.expressions.FirQualifiedAccess
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.research.ktglean.indexers.log
import org.jetbrains.research.ktglean.predicates.kotlin.v1.XRef
import org.jetbrains.research.ktglean.predicates.kotlin.v1.XRefTarget
import org.jetbrains.research.ktglean.predicates.unresolved
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanXRefFactory : KoinComponent {
    private val gleanClassFactory: GleanClassFactory by inject()
    private val gleanFunctionFactory: GleanFunctionFactory by inject()
    private val gleanPropertyFactory: GleanPropertyFactory by inject()

    fun getXRef(firQualifiedAccess: FirQualifiedAccess, context: CheckerContext): XRef? {
        return getTarget(firQualifiedAccess, context)?.let { XRef(it, firQualifiedAccess.loc) }
    }

    private fun getTarget(firQualifiedAccess: FirQualifiedAccess, context: CheckerContext): XRefTarget? {
        val target = firQualifiedAccess.callee ?: return unresolved()
        return XRefTarget(
            when (target) {
                is FirFunction ->
                    XRefTarget.Key.Function_(gleanFunctionFactory.getFunctionDeclaration(target, context))
                is FirProperty ->
                    XRefTarget.Key.Property_(gleanPropertyFactory.getProperty(target, context))
                is FirRegularClass ->
                    XRefTarget.Key.Class_(gleanClassFactory.getClassDeclaration(target, context))
                is FirTypeAlias ->
                    XRefTarget.Key.Class_(gleanClassFactory.getClassDeclaration(target.expandedTypeRef, context))
                is FirValueParameter -> return null
                else -> {
                    log(target, "t")
                    return unresolved()
                }
            }
        )
    }

    fun getXRef(firTypeRef: FirTypeRef, context: CheckerContext): XRef {
        return XRef(
            XRefTarget(XRefTarget.Key.Class_(gleanClassFactory.getClassDeclaration(firTypeRef, context))),
            firTypeRef.loc
        )
    }
}
