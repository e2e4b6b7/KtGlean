package org.jetbrains.research.ktglean.angle.data

data class Schema(
    val id: SchemaID,
    val types: Map<String, Type>,
    val predicates: List<Predicate>,
    val typeAliases: List<TypeAlias>
)
