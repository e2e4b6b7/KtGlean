package org.jetbrains.research.ktglean.predicates

import org.jetbrains.research.ktglean.predicates.types.GleanLoc
import org.jetbrains.research.ktglean.serialization.Fact

data class GleanFunctionCall(override val key: Key) : Fact {
    override val name get() = "kotlin.FunctionCall.1"

    data class Key(val function: GleanFunction, val loc: GleanLoc)
}
