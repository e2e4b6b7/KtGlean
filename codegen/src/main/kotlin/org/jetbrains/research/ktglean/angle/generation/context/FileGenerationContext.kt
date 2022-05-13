package org.jetbrains.research.ktglean.angle.generation.context

import com.squareup.kotlinpoet.*
import org.jetbrains.research.ktglean.angle.generation.PackageResolver

class FileGenerationContext(
    val fileBuilder: FileSpec.Builder,
    resolver: PackageResolver,
) : GenerationContext(resolver) {

    init {
        fileBuilder.suppressWarnings("RedundantVisibilityModifier", "unused")
    }

    override fun nestedClassName(name: String) = ClassName(fileBuilder.packageName, name)

    override fun addNestedClass(type: TypeSpec) {
        fileBuilder.addType(type)
    }
}

fun FileGenerationContext.addTypeAlias(name: String, type: TypeName) {
    fileBuilder.addTypeAlias(TypeAliasSpec.builder(name, type).build())
}
