package org.jetbrains.research.ktglean.angle.generation

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

data class KotlinClass(val name: TypeName, val spec: TypeSpec? = null)
