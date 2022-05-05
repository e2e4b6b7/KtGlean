package org.jetbrains.research.ktglean.angle.generation.context

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec
import org.jetbrains.research.ktglean.angle.generation.PackageResolver

class EnumGenerationContext(className: ClassName, resolver: PackageResolver) :
    ClassGenerationContext(className, TypeSpec.enumBuilder(className), resolver)

fun EnumGenerationContext.addConstant(name: String) {
    builder.addEnumConstant(name)
}
