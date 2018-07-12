package io.forus.me.android.data.entity.records.response;

import com.google.gson.annotations.SerializedName;

public class CreateRecordResult {

    @SerializedName("success")
    private Boolean success;

    public CreateRecordResult() {
    }

    public CreateRecordResult(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}