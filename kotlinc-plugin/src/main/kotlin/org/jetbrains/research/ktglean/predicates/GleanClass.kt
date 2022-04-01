package org.jetbrains.research.ktglean.predicates

import org.jetbrains.research.ktglean.serialization.Fact

data class GleanClass(override val key: Key) : Fact {
    override fun name() = "kotlin.Class.1"

    data class Key(val name: String, val supertypes: List<GleanClass>)
}
