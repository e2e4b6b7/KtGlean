package org.jetbrains.research.ktglean.angle.generation

import com.squareup.kotlinpoet.ClassName
import org.jetbrains.research.ktglean.angle.data.DefinitionID
import org.jetbrains.research.ktglean.angle.data.SchemaID

class PackageResolver(private val basePackage: String) {
    fun packageOf(schema: SchemaID): String {
        return "$basePackage.${schema.name}.v${schema.version}"
    }
}

fun PackageResolver.classNameOf(definition: DefinitionID): ClassName {
    return ClassName(packageOf(definition.schema), definition.name)
}
