package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.expressions.FirQualifiedAccess
import org.jetbrains.kotlin.fir.expressions.FirResolvedQualifier
import org.jetbrains.kotlin.fir.types.*
import org.jetbrains.research.ktglean.predicates.kotlin.v1.*
import org.jetbrains.research.ktglean.predicates.kotlin.v1.XRefTarget.Key.*
import org.jetbrains.research.ktglean.predicates.unresolved
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GleanXRefFactory : KoinComponent {
    private val classFactory: GleanClassFactory by inject()
    private val functionFactory: GleanFunctionFactory by inject()
    private val propertyFactory: GleanPropertyFactory by inject()

    fun getXRefs(firResolvedQualifier: FirResolvedQualifier, context: CheckerContext): List<XRef> = buildList {
        firResolvedQualifier.classId?.toFirClass(context)?.let { firClass ->
            add(classXRef(classFactory.getClassDeclaration(firClass, context), firResolvedQualifier))
        }
        typeArgumentsXRefs(firResolvedQualifier.typeArguments, context)
    }

    fun getXRefs(firQualifiedAccess: FirQualifiedAccess, context: CheckerContext): List<XRef> = buildList {
        getTarget(firQualifiedAccess, context)?.let { add(XRef(it, firQualifiedAccess.loc)) }
        typeArgumentsXRefs(firQualifiedAccess.typeArguments, context)
    }

    private fun MutableList<XRef>.typeArgumentsXRefs(
        typeArguments: List<FirTypeProjection>,
        context: CheckerContext
    ) {
        typeArguments.asSequence()
            .mapNotNull { it as? FirTypeProjectionWithVariance }
            .flatMapTo(this) { getXRefs(it.typeRef, context) }
    }

    private fun getTarget(firQualifiedAccess: FirQualifiedAccess, context: CheckerContext): XRefTarget? {
        val target = firQualifiedAccess.callee ?: return unresolved()
        val gleanTarget = when (target) {
            is FirFunction -> Function_(functionFactory.getFunctionDeclaration(target, context))
            is FirProperty -> Property_(propertyFactory.getProperty(target, context))
            is FirClass -> Class_(classFactory.getClassDeclaration(target, context))
            is FirTypeAlias -> Class_(classFactory.getClassDeclaration(target.expandedTypeRef, context))
            is FirValueParameter -> return null
            else -> return unresolved()
        }
        return XRefTarget(gleanTarget)
    }

    fun getXRefs(firTypeRef: FirTypeRef, context: CheckerContext): List<XRef> {
        return collectReferences(firTypeRef.coneType, context).map {
            classXRef(classFactory.getClassDeclaration(it, context), firTypeRef)
        }
    }

    private fun collectReferences(coneType: ConeKotlinType, context: CheckerContext): List<FirClass> = buildList {
        val coneTypes = mutableListOf<ConeKotlinType>()
        coneTypes.add(coneType)
        while (coneTypes.isNotEmpty()) {
            val type = coneTypes.last()
            coneTypes.pop()
            type.toFirClass(context)?.let { add(it) }
            type.typeArguments.forEach { typeProjection -> typeProjection.type?.let { coneTypes.add(it) } }
        }
    }

    private fun classXRef(declaration: ClassDeclaration, ref: FirElement) =
        XRef(XRefTarget(Class_(declaration)), ref.loc)
}
