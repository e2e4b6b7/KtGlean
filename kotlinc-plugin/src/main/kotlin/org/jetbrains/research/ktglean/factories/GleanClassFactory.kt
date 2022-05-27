package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.utils.classId
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.research.ktglean.predicates.kotlin.v1.ClassDeclaration
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.collections.set

class GleanClassFactory : KoinComponent {
    private val typeFactory: GleanTypeFactory by inject()
    private val cache = HashMap<ClassId, ClassDeclaration>()

    fun getClassDeclaration(firRegularClass: FirClass, context: CheckerContext): ClassDeclaration {
        cache[firRegularClass.classId]?.let { return it }

        val supers = firRegularClass.superTypeRefs.map { typeFactory.getTypeRef(it, context) }
        val params = firRegularClass.typeParameters.map { typeFactory.getTypeParameter(it, context) }
        val modifiers = firRegularClass.modifiersList
        val gleanClass = ClassDeclaration(
            firRegularClass.classId.qname,
            supers,
            params,
            modifiers,
            firRegularClass.loc
        )

        cache[firRegularClass.classId] = gleanClass

        return gleanClass
    }
}

fun GleanClassFactory.getClassDeclaration(firTypeRef: FirTypeRef, context: CheckerContext) =
    firTypeRef.toFirClass(context)?.let { getClassDeclaration(it, context) } ?: unresolved()
