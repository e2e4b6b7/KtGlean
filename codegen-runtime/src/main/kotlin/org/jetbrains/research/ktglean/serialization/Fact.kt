package org.jetbrains.research.ktglean.serialization

interface Fact {
    val key: Any

    val value: Any?
        get() = null

    fun name(): String
}
