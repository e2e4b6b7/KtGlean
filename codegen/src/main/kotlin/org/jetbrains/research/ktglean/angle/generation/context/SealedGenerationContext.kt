package org.jetbrains.research.ktglean.angle.generation.context

import com.squareup.kotlinpoet.*
import org.jetbrains.research.ktglean.angle.generation.PackageResolver

class SealedGenerationContext(className: ClassName, resolver: PackageResolver) :
    ClassGenerationContext(className, TypeSpec.interfaceBuilder(className), resolver) {

    init {
        builder.modifiers.add(KModifier.SEALED)
    }
}
