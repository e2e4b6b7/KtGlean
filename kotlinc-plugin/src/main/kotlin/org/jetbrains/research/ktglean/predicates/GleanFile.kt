package org.jetbrains.research.ktglean.predicates

import org.jetbrains.research.ktglean.serialization.Fact

data class GleanFile(override val key: String) : Fact {
    override val name get() = "kotlin.File.1"
}
