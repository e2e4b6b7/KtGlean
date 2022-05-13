@file:Suppress(
    "RedundantVisibilityModifier",
    "unused",
)

package org.jetbrains.research.ktglean.predicates.floatLiteralAnalysis.v1

import org.jetbrains.research.ktglean.predicates.kotlin.v1.FunctionCall
import org.jetbrains.research.ktglean.serialization.Fact

public data class FloatLiteralUsage(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "floatLiteralAnalysis.FloatLiteralUsage.1"

    public constructor(
        call: FunctionCall,
        literal: String,
        position: Int,
    ) : this(Key(call, literal, position))

    public data class Key(
        public val call: FunctionCall,
        public val literal: String,
        public val position: Int,
    )
}
