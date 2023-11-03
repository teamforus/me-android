package io.forus.me.android.data.entity.records.request;

import com.google.gson.annotations.SerializedName;

public class CreateValidationToken {

    @SerializedName("record_id")
    private Long record_id;

    public CreateValidationToken() {}

    public CreateValidationToken(Long record_id) {
        this.record_id = record_id;
    }

    public Long getRecord_id() {
        return record_id;
    }

    public void setRecord_id(Long record_id) {
        this.record_id = record_id;
    }
}
