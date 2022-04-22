package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.psi
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.parentsWithSelf
import org.jetbrains.research.ktglean.predicates.GleanRangeCall
import org.jetbrains.research.ktglean.predicates.GleanRangeCall.Context
import org.jetbrains.research.ktglean.predicates.GleanRangeCall.Context.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanRangeCallFactory : KoinComponent {
    private val functionCallFactory: GleanFunctionCallFactory by inject()

    fun getRangeCall(functionCall: FirFunctionCall, context: CheckerContext): GleanRangeCall {
        val gleanCall = functionCallFactory.getFunctionCall(functionCall, context)
        return GleanRangeCall(GleanRangeCall.Key(gleanCall, context(functionCall)))
    }

    private fun context(functionCall: FirFunctionCall): Context {
        val psi = functionCall.psi ?: return OTHER
        return psi.parentsWithSelf.mapNotNull { context(it) }.firstOrNull() ?: OTHER
    }

    private fun context(el: PsiElement): Context? {
        return when (el) {
            is KtForExpression -> FOR
            is KtWhileExpression -> WHILE
            is KtIfExpression -> IF
            is KtWhenExpression -> WHEN
            is KtProperty -> PROPERTY
            is KtDotQualifiedExpression ->
                when ((el.selectorExpression as? KtCallExpression)?.calleeExpression?.text) {
                    "forEach" -> FOREACH
                    "map" -> MAP
                    "toList" -> TOLIST
                    else -> null
                }
            is KtCallExpression -> if (el.calleeExpression?.text == "require") REQUIRE else null
            else -> null
        }
    }
}
