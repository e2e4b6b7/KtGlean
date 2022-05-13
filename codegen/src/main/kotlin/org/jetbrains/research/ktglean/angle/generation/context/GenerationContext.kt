package org.jetbrains.research.ktglean.angle.generation.context

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec
import org.jetbrains.research.ktglean.angle.generation.KotlinClass
import org.jetbrains.research.ktglean.angle.generation.PackageResolver

sealed class GenerationContext(val resolver: PackageResolver) {
    abstract fun nestedClassName(name: String): ClassName
    abstract fun addNestedClass(type: TypeSpec)
}

inline fun GenerationContext.innerClass(name: String, block: DataGenerationContext.() -> Unit): KotlinClass {
    return innerGeneral(name, block, ::DataGenerationContext)
}

inline fun GenerationContext.innerEnum(name: String, block: EnumGenerationContext.() -> Unit): KotlinClass {
    return innerGeneral(name, block, ::EnumGenerationContext)
}

inline fun GenerationContext.innerSealed(name: String, block: SealedGenerationContext.() -> Unit): KotlinClass {
    return innerGeneral(name, block, ::SealedGenerationContext)
}

inline fun <T : ClassGenerationContext> GenerationContext.innerGeneral(
    name: String,
    block: T.() -> Unit,
    constructor: (ClassName, PackageResolver) -> T
): KotlinClass {
    val innerClassName = nestedClassName(name)
    val innerContext = constructor(innerClassName, resolver)
    innerContext.block()
    val spec = innerContext.finish()
    addNestedClass(spec)
    return KotlinClass(innerClassName, spec)
}
