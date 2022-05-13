package org.jetbrains.research.ktglean.angle

import org.jetbrains.research.ktglean.angle.data.Schema
import org.jetbrains.research.ktglean.angle.data.SchemaID
import org.jetbrains.research.ktglean.angle.generation.generateSchema
import org.jetbrains.research.ktglean.angle.parsing.parseSchemas
import org.jetbrains.research.ktglean.angle.resolution.*
import java.nio.file.Files
import java.nio.file.Path

data class Command(val sources: List<Path>, val out: Path, val basePackage: String)

fun Command.generate() {
    sources.forEach {
        generate(it, out, basePackage)
    }
}

fun generate(src: Path, out: Path, basePackage: String) {
    val content = Files.readString(src)
    val parseTree = parseSchemas(content)
    val schemas = mutableMapOf<SchemaID, Lazy<Schema>>()
    parseTree.schema().asSequence()
        .map { DeclaredSchema(it) }
        .filter { !it.isRootSchema }
        .map { it.toUnresolved() }
        .groupingBy { it.id }
        .aggregateTo(schemas) { _, _, unresolvedSchema, first ->
            if (!first) error("Multiple schemas with same name: ${unresolvedSchema.id}")
            lazy { unresolvedSchema.resolve(schemas) }
        }
    schemas.values.forEach {
        generateSchema(it.value, basePackage, out)
    }
}
