package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.research.ktglean.predicates.GleanClass
import org.jetbrains.research.ktglean.predicates.GleanFunction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanFunctionFactory : KoinComponent {
    private val classBuilder: GleanClassFactory by inject()
    private val cache = HashMap<CallableId, GleanFunction>()

    fun getFunction(firFunction: FirFunction, context: CheckerContext): GleanFunction {
        val callableId = firFunction.symbol.callableId
        cache[callableId]?.let { return it }

        val gleanReturnClass = gleanReturnClass(firFunction, context) ?: GleanClass.UNRESOLVED
        val name = callableId.asSingleFqName().asString()
        val fact = GleanFunction(GleanFunction.Key(name, gleanReturnClass))

        cache[callableId] = fact

        return fact
    }

    private fun gleanReturnClass(firFunction: FirFunction, context: CheckerContext) =
        firFunction.returnTypeRef.firRegularClass(context)
            ?.let { firReturnClass -> classBuilder.getClass(firReturnClass, context) }
}
