package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirTypeParameter
import org.jetbrains.kotlin.fir.declarations.FirTypeParameterRef
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.types.*
import org.jetbrains.research.ktglean.predicates.kotlin.v1.*
import org.jetbrains.research.ktglean.predicates.unresolved
import org.koin.core.component.KoinComponent

class GleanTypeFactory : KoinComponent {
    private val cache = HashMap<ConeKotlinType, TypeRef>()

    fun getTypeRef(coneKotlinType: ConeKotlinType, context: CheckerContext): TypeRef {
        cache[coneKotlinType]?.let { return it }
        val gleanTypeRef = coneKotlinType.gleanTypeRef
        cache[coneKotlinType] = gleanTypeRef
        return gleanTypeRef
    }

    private val ConeKotlinType.gleanTypeRef: TypeRef
        get() = when (this) {
            is ConeFlexibleType -> upperBound.gleanTypeRef
//          is ConeDefinitelyNotNullType -> original.gleanTypeRef

            is ConeTypeParameterType -> gleanTypeParameterRef
            is ConeTypeVariableType -> gleanTypeParameterRef
            is ConeStubType -> constructor.variable.defaultType.gleanTypeParameterRef

            is ConeCapturedType -> constructor.projection.gleanTypeRef

            else -> gleanTypeExplicitRef
        }

    private val ConeTypeProjection.gleanTypeRef: TypeRef
        get() = when (this) {
            is ConeKotlinType -> gleanTypeRef
            is ConeKotlinTypeProjection -> type.gleanTypeRef
            ConeStarProjection -> TypeRef(TypeRef.Key.Star(Unit))
        }

    private val ConeLookupTagBasedType.gleanTypeParameterRef
        get() = TypeRef(TypeRef.Key.ParameterRef(lookupTag.name.asString()))

    private val ConeKotlinType.gleanTypeExplicitRef
        get() = TypeRef(TypeRef.Key.ExplicitRef(TypeRef.Key.ExplicitRef.ExplicitRef(classId?.qname ?: unresolved())))

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

fun GleanTypeFactory.getType(firTypeRef: FirTypeRef, context: CheckerContext): Type =
    Type(getTypeRef(firTypeRef, context), firTypeRef.coneType.nullability.gleanNullability)

fun GleanTypeFactory.getTypeRef(firTypeRef: FirTypeRef, context: CheckerContext): TypeRef =
    getTypeRef(firTypeRef.coneType, context)
