package org.jetbrains.research.ktglean.angle.generation

import com.squareup.kotlinpoet.FileSpec
import org.jetbrains.research.ktglean.angle.data.*
import org.jetbrains.research.ktglean.angle.generation.context.*
import java.nio.file.Path

fun generateSchema(schema: Schema, basePackage: String, rootDirectory: Path) {
    val resolver = PackageResolver(basePackage)
    val fileSpecBuilder = FileSpec.builder(resolver.packageOf(schema.id), "Schema")
    with(FileGenerationContext(fileSpecBuilder, resolver)) {
        for (typeAlias in schema.typeAliases) generateTypeAlias(typeAlias)
        for (predicate in schema.predicates) generatePredicate(predicate)
    }
    fileSpecBuilder.build().writeTo(rootDirectory)
}

private fun FileGenerationContext.generateTypeAlias(typeAlias: TypeAlias) {
    val kClass = generateType(typeAlias.type, typeAlias.id.name)
    if (kClass.spec == null) addTypeAlias(typeAlias.id.name, kClass.name)
}

private fun FileGenerationContext.generatePredicate(predicate: Predicate) {
    innerClass(predicate.id.name) {
        addSuperinterface(FACT_TYPE)
        addGetterProperty("name", STRING_TYPE, overridden = true) { addStatement("return %S", predicate.id) }
        val args = mutableListOf<KotlinClass>()

        val keyClass = generateType(predicate.key, "Key")
        addPrimaryProperty("key", keyClass.name, overridden = true)
        args.add(keyClass)

        predicate.value?.let {
            val valueClass = generateType(it, "Value")
            addPrimaryProperty("value", valueClass.name, overridden = true)
            args.add(valueClass)
        }

        addInlineConstructorByPrimary(args)
    }
}
