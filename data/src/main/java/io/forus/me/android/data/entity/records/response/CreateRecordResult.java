package io.forus.me.android.data.entity.records.response;

import com.google.gson.annotations.SerializedName;

public class CreateRecordResult {

    @SerializedName("success")
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}