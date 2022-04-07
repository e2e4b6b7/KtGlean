package org.jetbrains.research.ktglean.builders

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.FirRegularClass
import org.jetbrains.kotlin.fir.declarations.utils.classId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.research.ktglean.predicates.GleanClass
import org.koin.core.component.KoinComponent

class GleanClassBuilder : KoinComponent {
    private val cache = HashMap<ClassId, GleanClass>()

    fun getClass(firRegularClass: FirRegularClass, context: CheckerContext): GleanClass {
        cache[firRegularClass.classId]?.let { return it }

        val name = firRegularClass.classId.asFqNameString()
        val supers = firRegularClass.superTypeRefs.map { getClass(it.firRegularClass(context), context) }
        val gleanClass = GleanClass(GleanClass.Key(name, supers))

        cache[firRegularClass.classId] = gleanClass

        return gleanClass
    }
}
