package io.forus.me.android.data.entity.records.response;

import com.google.gson.annotations.SerializedName;

public class Validation {

    enum State {
        pending, approved, declined
    }

    @SerializedName("state")
    private State state;

    @SerializedName("identity_address")
    private Boolean identityAddress;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("value")
    private String value;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    public Validation() { }

    public Validation(State state, Boolean identityAddress, String uuid, String value, String key, String name) {
        this.state = state;
        this.identityAddress = identityAddress;
        this.uuid = uuid;
        this.value = value;
        this.key = key;
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Boolean getIdentityAddress() {
        return identityAddress;
    }

    public void setIdentityAddress(Boolean identityAddress) {
        this.identityAddress = identityAddress;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
