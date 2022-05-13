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
    val typeName = generateType(typeAlias.type, typeAlias.id.name)
    // If class was generated there is no need to generate typealias
    if (!isGenerated(typeName, typeAlias.id.name)) addTypeAlias(typeAlias.id.name, typeName)
}

private fun FileGenerationContext.generatePredicate(predicate: Predicate) {
    innerClass(predicate.id.name) {
        addSuperinterface(FACT_TYPE)
        addGetterProperty("name", STRING_TYPE, overridden = true) { addStatement("return %S", predicate.id) }
        addPrimaryProperty("key", generateType(predicate.key, "Key"), overridden = true)
        predicate.value?.let { addPrimaryProperty("value", generateType(it, "Value"), overridden = true) }
    }
}
