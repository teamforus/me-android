package io.forus.me.android.data.entity.validators.request;

import com.google.gson.annotations.SerializedName;

public class CreateValidationRequest {

    @SerializedName("validator_id")
    private Long validatorId;

    @SerializedName("record_id")
    private Long recordId;

    public CreateValidationRequest() { }

    public CreateValidationRequest(Long validatorId, Long recordId) {
        this.validatorId = validatorId;
        this.recordId = recordId;
    }

    public Long getValidatorId() {
        return validatorId;
    }

    public void setValidatorId(Long validatorId) {
        this.validatorId = validatorId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
}
