package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.*
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.resolve.firClassLike
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import org.jetbrains.research.ktglean.predicates.GleanFile
import org.jetbrains.research.ktglean.predicates.types.GleanLoc

fun FirTypeRef.firRegularClass(context: CheckerContext): FirRegularClass? {
    return when (val firDeclaration = firClassLike(context.session)) {
        is FirRegularClass -> firDeclaration
        is FirTypeAlias -> firDeclaration.expandedTypeRef.firRegularClass(context)
        is FirAnonymousObject -> null
        null -> null
    }
}

val FirElement.gleanLoc: GleanLoc
    get() {
        val psi = psi ?: return GleanLoc.UNRESOLVED
        val fileName = psi.containingFile.name
        return GleanLoc(GleanFile(fileName), psi.startOffset)
    }

@OptIn(SymbolInternals::class)
val FirFunctionCall.callee
    get() = calleeReference.resolvedSymbol?.fir as? FirFunction
