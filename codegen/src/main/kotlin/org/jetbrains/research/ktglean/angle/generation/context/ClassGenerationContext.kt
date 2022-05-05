package org.jetbrains.research.ktglean.angle.generation.context

import com.squareup.kotlinpoet.*
import org.jetbrains.research.ktglean.angle.generation.PackageResolver

sealed class ClassGenerationContext(
    val className: ClassName,
    val builder: TypeSpec.Builder,
    resolver: PackageResolver
) : GenerationContext(resolver) {
    override fun nestedClassName(name: String): ClassName {
        return className.nestedClass(name)
    }

    override fun addNestedClass(type: TypeSpec) {
        builder.addType(type)
    }

    open fun finish(): TypeSpec = builder.build()
}

fun ClassGenerationContext.addSuperinterface(type: TypeName) {
    builder.addSuperinterface(type)
}

inline fun ClassGenerationContext.addGetterProperty(
    name: String,
    type: TypeName,
    overridden: Boolean = false,
    getterConfig: FunSpec.Builder.() -> Unit
) {
    builder.addProperty(PropertySpec.builder(name, type).apply {
        if (overridden) addModifiers(KModifier.OVERRIDE)
        getter(FunSpec.getterBuilder().apply {
            getterConfig()
        }.build())
    }.build())
}
