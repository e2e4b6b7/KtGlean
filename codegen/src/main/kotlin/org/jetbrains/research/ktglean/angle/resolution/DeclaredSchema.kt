package org.jetbrains.research.ktglean.angle.resolution

import org.jetbrains.research.ktglean.angle.data.SchemaID
import org.jetbrains.research.ktglean.angle.data.schemaID
import org.jetbrains.research.ktglean.angle.parser.antlr.AngleParser

class DeclaredSchema(val tree: AngleParser.SchemaContext) {
    val id: SchemaID = tree.QVIDENT().schemaID
}

fun DeclaredSchema.toUnresolved(): UnresolvedSchema {
    val imports = imports
    val inherits = inherits
    validateReuses(imports, inherits)
    val inherit = validSingleInheritance(inherits)
    return UnresolvedSchema(id, inherit, imports, tree)
}

private fun validateReuses(imports: List<SchemaID>, inherits: List<SchemaID>) {
    val used = mutableSetOf<String>()
    (imports.asSequence() + inherits.asSequence()).forEach {
        if (used.contains(it.name))
            error("Schemas with same name included or inherited multiple times: ${it.name}")
        used.add(it.name)
    }
}

private fun DeclaredSchema.validSingleInheritance(inherits: List<SchemaID>): SchemaID? {
    if (inherits.isEmpty()) return null
    if (inherits.size > 1) error("Multiple inheritance is not supported")
    val inherit = inherits.first()
    if (inherit.name != id.name) error("Inheritance with different names is not supported")
    return inherit
}

val DeclaredSchema.imports: List<SchemaID>
    get() = tree.schemadecl().asSequence()
        .mapNotNull { it.import_() }
        .map { it.QVIDENT().schemaID }
        .toList()

val DeclaredSchema.inherits: List<SchemaID>
    get() = tree.inherit()?.QVIDENT()?.map { it.schemaID } ?: emptyList()

val DeclaredSchema.isRootSchema: Boolean
    get() = id.name == "all"
