package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.expressions.FirConstExpression
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.psi
import org.jetbrains.research.ktglean.predicates.floatLiteralAnalysis.v1.FloatLiteralUsage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanFloatLiteralUsageFactory : KoinComponent {
    private val functionCallFactory: GleanFunctionCallFactory by inject()

    fun getFloatLiteralUsage(
        functionCall: FirFunctionCall,
        literal: FirConstExpression<Float>,
        literalPos: Int,
        context: CheckerContext
    ): FloatLiteralUsage {
        val gleanCall = functionCallFactory.getFunctionCall(functionCall, context)
        val text = literal.psi?.text ?: unresolved()
        return FloatLiteralUsage(gleanCall, text, literalPos)
    }
}
