package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.research.ktglean.predicates.kotlin.v1.FunctionDeclaration
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanFunctionFactory : KoinComponent {
    private val typeFactory: GleanTypeFactory by inject()
    private val cache = HashMap<CallableId, FunctionDeclaration>()

    fun getFunctionDeclaration(firFunction: FirFunction, context: CheckerContext): FunctionDeclaration {
        val callableId = firFunction.symbol.callableId
        cache[callableId]?.let { return it }

        val returnType = typeFactory.getType(firFunction.returnTypeRef, context)
        val argTypes = firFunction.valueParameters.map { typeFactory.getType(it.returnTypeRef, context) }
        val typeParameters = firFunction.typeParameters.map { typeFactory.getTypeParameter(it, context) }
        val fact = FunctionDeclaration(
            callableId.qname,
            argTypes,
            typeParameters,
            returnType,
            firFunction.modifiersList,
            firFunction.loc
        )

        cache[callableId] = fact
        return fact
    }
}
