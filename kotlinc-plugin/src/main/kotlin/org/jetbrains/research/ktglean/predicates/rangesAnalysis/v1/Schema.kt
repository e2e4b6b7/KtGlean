@file:Suppress(
    "RedundantVisibilityModifier",
    "unused",
)

package org.jetbrains.research.ktglean.predicates.rangesAnalysis.v1

import org.jetbrains.research.ktglean.predicates.kotlin.v1.FunctionCall
import org.jetbrains.research.ktglean.serialization.Fact

public data class RangeCall(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "rangesAnalysis.RangeCall.1"

    public constructor(call: FunctionCall, context: Key.Context) : this(Key(call, context))

    public data class Key(
        public val call: FunctionCall,
        public val context: Context,
    ) {
        public enum class Context {
            FOR,
            WHILE,
            IF,
            WHEN,
            PROPERTY,
            FOREACH,
            MAP,
            TOLIST,
            REQUIRE,
            OTHER,
        }
    }
}
