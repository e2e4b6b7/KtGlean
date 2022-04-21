package org.jetbrains.research.ktglean.serialization

import com.google.gson.*
import java.lang.reflect.Type

object EnumSerializer : JsonSerializer<Enum<*>> {
    override fun serialize(src: Enum<*>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.ordinal)
    }
}
