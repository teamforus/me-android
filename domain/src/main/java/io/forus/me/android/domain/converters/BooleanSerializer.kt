package io.forus.me.android.domain.converters


import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type

class BooleanSerializer : JsonSerializer<Boolean>, JsonDeserializer<Boolean> {

    override fun serialize(arg0: Boolean, arg1: Type, arg2: JsonSerializationContext): JsonElement {
        return JsonPrimitive(if (arg0) 1 else 0)
    }

    @Throws(JsonParseException::class)
    override fun deserialize(arg0: JsonElement, arg1: Type, arg2: JsonDeserializationContext): Boolean? {
        val valueString = arg0.asString ?: return false

        if ("1".equals(valueString, ignoreCase = true))
            return true

        if ("0".equals(valueString, ignoreCase = true))
            return false


        return if ("NULL".equals(valueString, ignoreCase = true)) false else java.lang.Boolean.valueOf(valueString)

    }
}