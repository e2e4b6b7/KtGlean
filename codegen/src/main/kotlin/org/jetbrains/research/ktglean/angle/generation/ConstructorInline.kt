package org.jetbrains.research.ktglean.angle.generation

import com.squareup.kotlinpoet.*
import org.jetbrains.research.ktglean.angle.generation.context.ClassGenerationContext
import org.jetbrains.research.ktglean.angle.generation.context.addConstructor

data class KotlinClassConstructor(val name: TypeName, val constructor: FunSpec)

fun ClassGenerationContext.addInlineConstructorByPrimary(vararg classes: KotlinClass) {
    addInlineConstructorByPrimary(classes.asList())
}

fun ClassGenerationContext.addInlineConstructorByPrimary(classes: List<KotlinClass>) {
    val inlinedConstructors = classes.mapNotNull { kClass ->
        kClass.spec?.primaryConstructor?.let { primaryConstructor ->
            KotlinClassConstructor(kClass.name, primaryConstructor)
        }
    }
    if (classes.size != inlinedConstructors.size) return
    addInlineConstructor(inlinedConstructors)
}

fun ClassGenerationContext.addInlineConstructor(constructors: List<KotlinClassConstructor>) = addConstructor {
    val blockBuilder = CodeBlock.builder()
    constructors.forEach { (name, constructor) ->
        addParameters(constructor.parameters)

        val argsFormat = generateSequence { "%N" }.take(constructor.parameters.size).joinToString()
        val argsNames = constructor.parameters.map { it.name }.toTypedArray()
        blockBuilder.add("%T($argsFormat)", name, *argsNames)
    }
    callThisConstructor(blockBuilder.build())
}
