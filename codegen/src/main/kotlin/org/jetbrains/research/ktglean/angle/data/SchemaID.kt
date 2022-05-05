package org.jetbrains.research.ktglean.angle.data

import org.antlr.v4.runtime.tree.TerminalNode

data class SchemaID(val name: String, val version: Int) {
    override fun toString() = "$name.$version"
}

fun SchemaID.definition(name: String) = DefinitionID(this, name)

/**
 * Terminal node is expected to be QVIDENT.
 */
val TerminalNode.schemaID: SchemaID
    get() = text.run {
        val del = lastIndexOf('.')
        val version = substring(del + 1).toInt()
        val name = substring(0, del)
        return SchemaID(name, version)
    }
