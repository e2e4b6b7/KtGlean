package org.jetbrains.research.ktglean.angle.generation

import com.squareup.kotlinpoet.ClassName

val BYTE_TYPE = ClassName("kotlin", "Byte")
val NAT_TYPE = ClassName("kotlin", "Int")
val BOOL_TYPE = ClassName("kotlin", "Boolean")
val STRING_TYPE = ClassName("kotlin", "String")
val UNIT_TYPE = ClassName("kotlin", "Unit")
val ARRAY_TYPE = ClassName("kotlin.collections", "List")

val FACT_TYPE = ClassName("org.jetbrains.research.ktglean.serialization", "Fact")

val SUPPRESS_TYPE = ClassName("kotlin", "Suppress")

val BYTE_CLASS = KotlinClass(BYTE_TYPE)
val NAT_CLASS = KotlinClass(NAT_TYPE)
val BOOL_CLASS = KotlinClass(BOOL_TYPE)
val STRING_CLASS = KotlinClass(STRING_TYPE)
val UNIT_CLASS = KotlinClass(UNIT_TYPE)
