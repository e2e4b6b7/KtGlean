package org.jetbrains.research.ktglean.angle.generation.context

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec
import org.jetbrains.research.ktglean.angle.generation.PackageResolver

sealed class GenerationContext(val resolver: PackageResolver) {
    abstract fun nestedClassName(name: String): ClassName
    abstract fun addNestedClass(type: TypeSpec)
}

inline fun GenerationContext.innerClass(name: String, block: DataGenerationContext.() -> Unit): ClassName {
    return innerGeneral(name, block, ::DataGenerationContext)
}

inline fun GenerationContext.innerEnum(name: String, block: EnumGenerationContext.() -> Unit): ClassName {
    return innerGeneral(name, block, ::EnumGenerationContext)
}

inline fun GenerationContext.innerSealed(name: String, block: SealedGenerationContext.() -> Unit): ClassName {
    return innerGeneral(name, block, ::SealedGenerationContext)
}

inline fun <T : ClassGenerationContext> GenerationContext.innerGeneral(
    name: String,
    block: T.() -> Unit,
    constructor: (ClassName, PackageResolver) -> T
): ClassName {
    val innerClassName = nestedClassName(name)
    val innerContext = constructor(innerClassName, resolver)
    innerContext.block()
    addNestedClass(innerContext.finish())
    return innerClassName
}
