package io.forus.me.android.data.entity.records.response;

import com.google.gson.annotations.SerializedName;

public class RecordType {

    @SerializedName("key")
    private String key;

    @SerializedName("type")
    private String type;

    @SerializedName("name")
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
