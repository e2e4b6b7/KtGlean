package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.research.ktglean.predicates.GleanFunction
import org.jetbrains.research.ktglean.predicates.GleanFunctionCall
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanFunctionCallFactory : KoinComponent {
    private val functionBuilder: GleanFunctionFactory by inject()

    fun getFunctionCall(functionCall: FirFunctionCall, context: CheckerContext): GleanFunctionCall {
        val gleanCallee = gleanCallee(functionCall, context) ?: GleanFunction.UNRESOLVED
        return GleanFunctionCall(GleanFunctionCall.Key(gleanCallee, functionCall.gleanLoc))
    }

    private fun gleanCallee(functionCall: FirFunctionCall, context: CheckerContext): GleanFunction? {
        val func = functionCall.callee ?: return null
        return functionBuilder.getFunction(func, context)
    }
}
