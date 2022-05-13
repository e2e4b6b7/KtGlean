package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirProperty
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.research.ktglean.predicates.kotlin.v1.PropertyDeclaration
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanPropertyFactory : KoinComponent {
    private val typeFactory: GleanTypeFactory by inject()
    private val cache = HashMap<CallableId, PropertyDeclaration>()

    fun getProperty(firProperty: FirProperty, context: CheckerContext): PropertyDeclaration {
        val callableId = firProperty.symbol.callableId
        cache[callableId]?.let { return it }

        val returnType = typeFactory.getType(firProperty.returnTypeRef, context)
        val typeParameters = firProperty.typeParameters.map { typeFactory.getTypeParameter(it, context) }
        val fact = PropertyDeclaration(
            callableId.qname,
            typeParameters,
            returnType,
            firProperty.setter != null,
            firProperty.modifiersList,
            firProperty.loc
        )

        cache[callableId] = fact
        return fact
    }
}
