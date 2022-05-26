package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirTypeParameter
import org.jetbrains.kotlin.fir.declarations.FirTypeParameterRef
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.types.*
import org.jetbrains.research.ktglean.predicates.kotlin.v1.*
import org.jetbrains.research.ktglean.predicates.kotlin.v1.TypeRef.Key.*
import org.jetbrains.research.ktglean.predicates.unresolved
import org.koin.core.component.KoinComponent

class GleanTypeFactory : KoinComponent {
    private val cache = HashMap<ConeKotlinType, TypeRef>()

    fun getTypeRef(coneKotlinType: ConeKotlinType, context: CheckerContext): TypeRef {
        cache[coneKotlinType]?.let { return it }
        val gleanTypeRef = coneKotlinType.gleanTypeRef(context)
        cache[coneKotlinType] = gleanTypeRef
        return gleanTypeRef
    }

    private fun ConeKotlinType.gleanTypeRef(context: CheckerContext): TypeRef = when (this) {
        is ConeFlexibleType -> upperBound.gleanTypeRef(context)
        is ConeDefinitelyNotNullType -> original.gleanTypeRef(context)

        is ConeTypeParameterType -> gleanTypeParameterRef()
        is ConeTypeVariableType -> gleanTypeParameterRef()
        is ConeStubType -> constructor.variable.defaultType.gleanTypeParameterRef()

        is ConeCapturedType -> constructor.projection.gleanTypeRef(context)

        else -> gleanTypeExplicitRef(context)
    }

    private fun ConeTypeProjection.gleanTypeRef(context: CheckerContext): TypeRef = when (this) {
        is ConeKotlinType -> gleanTypeRef(context)
        is ConeKotlinTypeProjection -> type.gleanTypeRef(context)
        ConeStarProjection -> TypeRef(Star(Unit))
    }

    private fun ConeLookupTagBasedType.gleanTypeParameterRef() = TypeRef(ParameterRef(lookupTag.name.asString()))

    private fun ConeKotlinType.gleanTypeExplicitRef(context: CheckerContext) =
        TypeRef(
            ExplicitRef(
                ExplicitRef.ExplicitRef(
                    classId?.qname ?: unresolved(),
                    typeArguments.mapNotNull { typeProjection -> typeProjection.type?.let { getType(it, context) } })
            )
        )
}

fun GleanTypeFactory.getTypeParameter(firTypeParameter: FirTypeParameter, context: CheckerContext): TypeParameter =
    TypeParameter(
        firTypeParameter.name.asString(),
        firTypeParameter.isReified,
        firTypeParameter.bounds.map { getType(it, context) },
        firTypeParameter.variance.gleanVariance
    )

@OptIn(SymbolInternals::class)
fun GleanTypeFactory.getTypeParameter(
    firTypeParameterRef: FirTypeParameterRef,
    context: CheckerContext
): TypeParameter = getTypeParameter(firTypeParameterRef.symbol.fir, context)

fun GleanTypeFactory.getType(coneType: ConeKotlinType, context: CheckerContext): Type =
    Type(getTypeRef(coneType, context), coneType.nullability.gleanNullability)

fun GleanTypeFactory.getType(firTypeRef: FirTypeRef, context: CheckerContext): Type =
    getType(firTypeRef.coneType, context)

fun GleanTypeFactory.getTypeRef(firTypeRef: FirTypeRef, context: CheckerContext): TypeRef =
    getTypeRef(firTypeRef.coneType, context)
