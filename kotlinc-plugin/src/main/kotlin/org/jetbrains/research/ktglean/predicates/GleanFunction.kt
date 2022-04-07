package org.jetbrains.research.ktglean.predicates

import org.jetbrains.research.ktglean.serialization.Fact

data class GleanFunction(override val key: Key) : Fact {
    override val name get() = "kotlin.Function.1"

    data class Key(val name: String, val returnType: GleanClass)
}
