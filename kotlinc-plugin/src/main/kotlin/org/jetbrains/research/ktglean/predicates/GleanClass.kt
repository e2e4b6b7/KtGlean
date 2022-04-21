package org.jetbrains.research.ktglean.predicates

import org.jetbrains.research.ktglean.serialization.Fact

data class GleanClass(override val key: Key) : Fact {
    override val name get() = "kotlin.Class.1"

    data class Key(val name: String, val supertypes: List<GleanClass>)

    companion object {
        val UNRESOLVED = GleanClass(Key("UNRESOLVED", emptyList()))
    }
}
