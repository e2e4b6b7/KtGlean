package org.jetbrains.research.ktglean.predicates

import org.jetbrains.research.ktglean.serialization.Fact

data class GleanRangeCall(override val key: Key) : Fact {
    override val name: String get() = "rangesAnalysis.RangeCall.1"

    data class Key(val call: GleanFunctionCall, val context: Context)

    enum class Context {
        FOR,
        WHILE,
        IF,
        WHEN,
        PROPERTY,
        FOREACH,
        MAP,
        TOLIST,
        REQUIRE,
        OTHER;
    }
}
