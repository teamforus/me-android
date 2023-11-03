package io.forus.me.android.data.entity.records.response;

import com.google.gson.annotations.SerializedName;

public class ValidationToken {

    @SerializedName("uuid")
    private String uuid;

    public ValidationToken() { }

    public ValidationToken(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
