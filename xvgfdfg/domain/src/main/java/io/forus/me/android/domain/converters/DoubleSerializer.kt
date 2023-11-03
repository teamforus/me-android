package io.forus.me.android.domain.converters

/**
 * Created by pavel on 22.05.17.
 */

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type

class DoubleSerializer : JsonSerializer<Double>, JsonDeserializer<Double> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Double? {
        val valueString = json.asString ?: return 0f.toDouble()

        return java.lang.Double.valueOf(valueString)
    }

    override fun serialize(src: Double?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(String.format("%.10f", src).replace(",", "."))
    }
}