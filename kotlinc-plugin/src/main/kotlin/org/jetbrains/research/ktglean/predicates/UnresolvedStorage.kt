package org.jetbrains.research.ktglean.predicates

import org.jetbrains.research.ktglean.predicates.kotlin.v1.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

object UnresolvedStorage {
    private val unresolvedMap = ConcurrentHashMap<KClass<*>, Lazy<*>>()

    init {
        addUnresolved { -1 }
        addUnresolved { "UNRESOLVED" }
        addUnresolved { emptyList<Nothing>() }
        addUnresolved { File(unresolved()) }
        addUnresolved { Loc(unresolved(), unresolved()) }
        addUnresolved { Package(unresolved()) }
        addUnresolved { QName(unresolved(), unresolved()) }
        addUnresolved { ClassDeclaration(unresolved(), unresolved(), unresolved(), unresolved(), unresolved()) }
        addUnresolved { XRefTarget(XRefTarget.Key.Class_(unresolved())) }
    }

    fun unresolved(clazz: KClass<*>): Any? = unresolvedMap[clazz]?.value

    private inline fun <reified T : Any> addUnresolved(crossinline value: () -> T) {
        unresolvedMap[T::class] = lazy { value() }
    }
}

inline fun <reified T : Any> unresolved(): T {
    return UnresolvedStorage.unresolved(T::class) as? T ?: error("unresolved")
}
