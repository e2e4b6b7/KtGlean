package org.jetbrains.research.ktglean.angle.data

data class DefinitionID(val schema: SchemaID, val name: String) {
    override fun toString() = "${schema.name}.$name.${schema.version}"
}
