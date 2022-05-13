package org.jetbrains.research.ktglean.angle.generation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

fun isGenerated(typeName: TypeName, expectedName: String) =
    typeName is ClassName && typeName.simpleName == expectedName
