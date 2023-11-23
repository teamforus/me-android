package io.forus.me.android.data.entity.validators.response;

import com.google.gson.annotations.SerializedName;

public class ValidationRequest {

    public enum State {
        pending, approved, declined
    }

    @SerializedName("id")
    private Long id;

    @SerializedName("validator_id")
    private Long validatorId;

    @SerializedName("record_validation_uid")
    private Long recordValidationUid;

    @SerializedName("identity_address")
    private String identityAddress;

    @SerializedName("record_id")
    private Long recordId;

    @SerializedName("state")
    private State state;

    @SerializedName("validator")
    private Validator validator;

    public ValidationRequest() { }

    public ValidationRequest(Long id, Long validatorId, Long recordValidationUid, String identityAddress, Long recordId, State state, Validator validator) {
        this.id = id;
        this.validatorId = validatorId;
        this.recordValidationUid = recordValidationUid;
        this.identityAddress = identityAddress;
        this.recordId = recordId;
        this.state = state;
        this.validator = validator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValidatorId() {
        return validatorId;
    }

    public void setValidatorId(Long validatorId) {
        this.validatorId = validatorId;
    }

    public Long getRecordValidationUid() {
        return recordValidationUid;
    }

    public void setRecordValidationUid(Long recordValidationUid) {
        this.recordValidationUid = recordValidationUid;
    }

    public String getIdentityAddress() {
        return identityAddress;
    }

    public void setIdentityAddress(String identityAddress) {
        this.identityAddress = identityAddress;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
}
