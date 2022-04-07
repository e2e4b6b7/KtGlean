package org.jetbrains.research.ktglean.builders

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirSimpleFunction
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.research.ktglean.predicates.GleanFunction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanFunctionBuilder : KoinComponent {
    private val classBuilder: GleanClassBuilder by inject()
    private val cache = HashMap<CallableId, GleanFunction>()

    fun getFunction(firFunction: FirSimpleFunction, context: CheckerContext): GleanFunction {
        val callableId = firFunction.symbol.callableId
        cache[callableId]?.let { return it }

        val returnGleanClass = classBuilder.getClass(firFunction.returnTypeRef.firRegularClass(context), context)
        val name = callableId.asSingleFqName().asString()
        val fact = GleanFunction(GleanFunction.Key(name, returnGleanClass))

        cache[callableId] = fact

        return fact
    }
}
