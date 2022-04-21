package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.psi
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.parentsWithSelf
import org.jetbrains.research.ktglean.predicates.GleanRangeCall
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanRangeCallFactory : KoinComponent {
    private val functionCallFactory: GleanFunctionCallFactory by inject()

    fun getRangeCall(functionCall: FirFunctionCall, context: CheckerContext): GleanRangeCall {
        val gleanCall = functionCallFactory.getFunctionCall(functionCall, context)
        return GleanRangeCall(GleanRangeCall.Key(gleanCall, context(functionCall)))
    }

    private fun context(functionCall: FirFunctionCall): GleanRangeCall.Context {
        val psi = functionCall.psi ?: return GleanRangeCall.Context.OTHER
        return psi.parentsWithSelf.mapNotNull { context(it) }.firstOrNull() ?: GleanRangeCall.Context.OTHER
    }

    private fun context(el: PsiElement): GleanRangeCall.Context? {
        return when (el) {
            is KtForExpression -> GleanRangeCall.Context.FOR
            is KtWhileExpression -> GleanRangeCall.Context.WHILE
            is KtIfExpression -> GleanRangeCall.Context.IF
            is KtWhenExpression -> GleanRangeCall.Context.WHEN
            is KtProperty -> GleanRangeCall.Context.PROPERTY
            else -> null
        }
    }
}

