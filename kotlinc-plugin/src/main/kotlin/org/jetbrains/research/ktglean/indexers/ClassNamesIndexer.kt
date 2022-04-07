package org.jetbrains.research.ktglean.indexers

import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.utils.classId
import org.jetbrains.kotlin.fir.resolve.firClassLike
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.research.ktglean.indexers.base.FirClassIndexer
import org.jetbrains.research.ktglean.predicates.GleanClass
import org.jetbrains.research.ktglean.storage.FactsStorage

class ClassNamesIndexer(private val storage: FactsStorage) : FirClassIndexer() {
    private val cache = HashMap<ClassId, GleanClass>()

    override fun index(declaration: FirClass, context: CheckerContext) {
        if (declaration is FirRegularClass) {
            storage.addFact(context.getClass(declaration))
        }
    }

    private fun CheckerContext.resolve(ref: FirTypeRef): FirRegularClass {
        return when (val firDeclaration = ref.firClassLike(session)) {
            is FirRegularClass -> firDeclaration
            is FirTypeAlias -> resolve(firDeclaration.expandedTypeRef)
            is FirAnonymousObject -> error("Inheritance from an anonymous class")
            null -> error("Expecting resolved declaration")
        }
    }

    private fun CheckerContext.getClass(firRegularClass: FirRegularClass): GleanClass {
        cache[firRegularClass.classId]?.let { return it }

        val name = firRegularClass.classId.asFqNameString()
        val supers = firRegularClass.superTypeRefs.map { getClass(resolve(it)) }
        val gleanClass = GleanClass(GleanClass.Key(name, supers))

        cache[firRegularClass.classId] = gleanClass

        return gleanClass
    }
}
