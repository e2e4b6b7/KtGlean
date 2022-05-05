package org.jetbrains.research.ktglean.angle.data

sealed interface Type

object NatType : Type

object StringType : Type

object BoolType : Type

data class RecordType(val fields: Map<String, Type>) : Type

data class ReferenceType(val id: DefinitionID) : Type

data class ArrayType(val elementType: Type) : Type

data class MaybeType(val originalType: Type) : Type

data class EnumType(val values: List<String>) : Type

data class SumType(val options: Map<String, Type>) : Type
