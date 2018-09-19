package io.forus.me.android.data.entity.serializers;

import com.fernandocejas.arrow.strings.Strings;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import io.forus.me.android.data.entity.sign.request.SignRecords;

public class SignRecordsTypeAdapter extends TypeAdapter<SignRecords> {

    @Override
    public void write(JsonWriter out, SignRecords value) throws IOException {
        out.beginObject();
        if (!Strings.isNullOrEmpty(value.getEmail())) {
            out.name("primary_email");
            out.value(value.getEmail());
        }

        if (!Strings.isNullOrEmpty(value.getFirstname())) {
            out.name("given_name");
            out.value(value.getFirstname());
        }

        if (!Strings.isNullOrEmpty(value.getLastname())) {
            out.name("family_name");
            out.value(value.getLastname());
        }

        if (!Strings.isNullOrEmpty(value.getBsn())) {
            out.name("bsn");
            out.value(value.getBsn());
        }

        if (!Strings.isNullOrEmpty(value.getPhoneNumber())) {
            out.name("telephone");
            out.value(value.getPhoneNumber());
        }

        out.endObject();
    }

    @Override
    public SignRecords read(JsonReader in) {
        return null;
    }
}
