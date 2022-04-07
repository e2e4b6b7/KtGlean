package org.jetbrains.research.ktglean.serialization

import com.google.gson.*
import java.lang.reflect.Type

object FactsSerializer : JsonSerializer<Collection<Fact>> {
    override fun serialize(
        src: Collection<Fact>,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val array = JsonArray()
        src.asSequence()
            .map(context::serialize)
            // remain only new predicates (which serialized not as id reference)
            .filterIsInstance<JsonObject>()
            .forEach(array::add)
        return array
    }
}
