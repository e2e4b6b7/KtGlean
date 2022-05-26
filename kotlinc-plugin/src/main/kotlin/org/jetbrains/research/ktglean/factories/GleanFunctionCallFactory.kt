package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.research.ktglean.predicates.kotlin.v1.FunctionCall
import org.jetbrains.research.ktglean.predicates.kotlin.v1.FunctionDeclaration
import org.jetbrains.research.ktglean.predicates.unresolved
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanFunctionCallFactory : KoinComponent {
    private val functionBuilder: GleanFunctionFactory by inject()

    fun getFunctionCall(functionCall: FirFunctionCall, context: CheckerContext): FunctionCall {
        val gleanCallee = gleanCallee(functionCall, context) ?: unresolved()
        return FunctionCall(gleanCallee, functionCall.loc)
    }

    private fun gleanCallee(functionCall: FirFunctionCall, context: CheckerContext): FunctionDeclaration? {
        val func = functionCall.callee ?: return null
        return functionBuilder.getFunctionDeclaration(func, context)
    }
}
