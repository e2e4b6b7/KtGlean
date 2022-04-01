package org.jetbrains.research.ktglean.serialization

import com.google.gson.*
import java.lang.reflect.Type

class BackReferenceSerializer : JsonSerializer<Fact> {
    private val factID = HashMap<Fact, Int>()

    override fun serialize(src: Fact, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        factID[src]?.let { return JsonPrimitive(it) }

        val id = factID.size + 1
        factID[src] = id

        val obj = JsonObject()
        obj.addProperty("id", id)
        obj.add("key", context.serialize(src.key))
        src.value?.let { obj.add("value", context.serialize(it)) }
        return obj
    }
}
