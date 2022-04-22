package org.jetbrains.research.ktglean.predicates

import org.jetbrains.research.ktglean.serialization.Fact

data class GleanFloatLiteralUsage(override val key: Key) : Fact {
    override val name: String get() = "floatLiteralAnalysis.FloatLiteralUsage.1"

    data class Key(val call: GleanFunctionCall, val literal: String, val position: Int)
}
