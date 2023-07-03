package io.forus.me.android.data.entity.serializers

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.forus.me.android.data.entity.sign.request.SignRecords
import java.io.IOException

class SignRecordsTypeAdapter : TypeAdapter<SignRecords?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: SignRecords?) {
        out.beginObject()

        value?.firstname?.let {
            out.name("given_name")
            out.value(value.firstname)
        }

        value?.lastname?.let {
            out.name("family_name")
            out.value(value.lastname)
        }

        value?.bsn?.let {
            out.name("bsn")
            out.value(value.bsn)
        }

        value?.phoneNumber?.let {
            out.name("telephone")
            out.value(value.phoneNumber)
        }

        out.endObject()
    }

    override fun read(`in`: JsonReader): SignRecords? {
        return null
    }
}