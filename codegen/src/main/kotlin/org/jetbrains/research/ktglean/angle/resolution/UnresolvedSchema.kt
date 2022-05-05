package org.jetbrains.research.ktglean.angle.resolution

import org.jetbrains.research.ktglean.angle.data.*
import org.jetbrains.research.ktglean.angle.parser.antlr.AngleParser

class UnresolvedSchema(
    val id: SchemaID,
    val inherit: SchemaID?,
    val imports: List<SchemaID>,
    val tree: AngleParser.SchemaContext
)

fun UnresolvedSchema.resolve(resolved: Map<SchemaID, Lazy<Schema>>): Schema {
    val unqualifiedTypes = unqualifiedContext(resolved)
    val qualifiedTypes = qualifiedContext(unqualifiedTypes, resolved)
    val typeResolutionContext = TypeResolutionContext(unqualifiedTypes, qualifiedTypes)
    val resolver = TypeResolver(typeResolutionContext)
    return Schema(id, unqualifiedTypes, collectPredicates(resolver), collectTypeAliases(resolver))
}

private fun UnresolvedSchema.qualifiedContext(
    unqualifiedTypes: Map<String, Type>,
    resolved: Map<SchemaID, Lazy<Schema>>
): Map<String, Map<String, Type>> {
    val qualifiedTypes = HashMap<String, Map<String, Type>>()
    imports.forEach {
        val importedSchema = resolved[it] ?: error("No such schema $it")
        qualifiedTypes[it.name] = importedSchema.value.types
    }
    qualifiedTypes[id.name] = unqualifiedTypes
    return qualifiedTypes
}

private fun UnresolvedSchema.unqualifiedContext(resolved: Map<SchemaID, Lazy<Schema>>): Map<String, Type> {
    val unqualifiedTypes = HashMap<String, Type>()
    inherit?.let {
        val inheritedSchema = resolved[it] ?: error("No such schema $it")
        unqualifiedTypes.putAll(inheritedSchema.value.types)
    }
    tree.schemadecl().forEach {
        it.predicate()?.ident()?.text?.let { unqualifiedTypes[it] = ReferenceType(id.definition(it)) }
        it.typedef()?.ident()?.text?.let { unqualifiedTypes[it] = ReferenceType(id.definition(it)) }
    }
    return unqualifiedTypes
}

private fun UnresolvedSchema.collectTypeAliases(typeResolver: TypeResolver): List<TypeAlias> {
    return tree.schemadecl().asSequence()
        .mapNotNull { it.typedef() }
        .map { typeCtx ->
            val definitionID = id.definition(typeCtx.ident().text)
            val type = typeResolver.resolve(typeCtx.type())
            TypeAlias(definitionID, type)
        }
        .toList()
}

private fun UnresolvedSchema.collectPredicates(typeResolver: TypeResolver): List<Predicate> {
    return tree.schemadecl().asSequence()
        .mapNotNull { it.predicate() }
        .map { predicateCtx ->
            val predicateID = id.definition(predicateCtx.ident().text)
            val keyType = typeResolver.resolve(predicateCtx.key().type())
            val valueType = predicateCtx.value()?.let { typeResolver.resolve(it.type()) }
            Predicate(predicateID, keyType, valueType)
        }
        .toList()
}
