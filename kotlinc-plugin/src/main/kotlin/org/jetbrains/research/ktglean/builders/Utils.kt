package org.jetbrains.research.ktglean.builders

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.resolve.firClassLike
import org.jetbrains.kotlin.fir.types.FirTypeRef

fun FirTypeRef.firRegularClass(context: CheckerContext): FirRegularClass {
    return when (val firDeclaration = firClassLike(context.session)) {
        is FirRegularClass -> firDeclaration
        is FirTypeAlias -> firDeclaration.expandedTypeRef.firRegularClass(context)
        is FirAnonymousObject -> error("Unexpected anonymous object")
        null -> error("Expecting resolved references")
    }
}
