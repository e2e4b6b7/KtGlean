package org.jetbrains.research.ktglean.angle.generation.context

import com.squareup.kotlinpoet.*
import org.jetbrains.research.ktglean.angle.generation.PackageResolver

class DataGenerationContext(className: ClassName, resolver: PackageResolver) :
    ClassGenerationContext(className, TypeSpec.classBuilder(className), resolver) {

    private val primaryConstructorBuilder = FunSpec.constructorBuilder()

    init {
        builder.modifiers.add(KModifier.DATA)
    }

    fun addPrimaryProperty(spec: PropertySpec) {
        primaryConstructorBuilder.addParameter(spec.name, spec.type)
        builder.addProperty(spec.toBuilder().initializer(spec.name).build())
    }

    override fun finish(): TypeSpec {
        builder.primaryConstructor(primaryConstructorBuilder.build())
        return super.finish()
    }
}

fun DataGenerationContext.addPrimaryProperty(name: String, type: TypeName, overridden: Boolean = false) {
    addPrimaryProperty(PropertySpec.builder(name, type).apply {
        if (overridden) addModifiers(KModifier.OVERRIDE)
    }.build())
}
