package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.expressions.FirConstExpression
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.psi
import org.jetbrains.research.ktglean.predicates.GleanFloatLiteralUsage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanFloatLiteralUsageFactory : KoinComponent {
    private val functionCallBuilder: GleanFunctionCallFactory by inject()

    fun getFloatLiteralUsage(
        functionCall: FirFunctionCall,
        literal: FirConstExpression<Float>,
        literalPos: Int,
        context: CheckerContext
    ): GleanFloatLiteralUsage {
        val gleanCall = functionCallBuilder.getFunctionCall(functionCall, context)
        val text = literal.psi?.text ?: "UNRESOLVED"
        return GleanFloatLiteralUsage(GleanFloatLiteralUsage.Key(gleanCall, text, literalPos))
    }
}
