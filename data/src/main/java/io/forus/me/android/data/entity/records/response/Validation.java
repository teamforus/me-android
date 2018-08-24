package io.forus.me.android.data.entity.records.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Validation {

    public enum State {
        pending, approved, declined
    }

    @SerializedName("state")
    private State state;

    @SerializedName("identity_address")
    private String identityAddress;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("value")
    private String value;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    public Validation() { }

    public Validation(State state, String identityAddress, Date createdAt, Date updatedAt, String uuid, String value, String key, String name) {
        this.state = state;
        this.identityAddress = identityAddress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getIdentityAddress() {
        return identityAddress;
    }

    public void setIdentityAddress(String identityAddress) {
        this.identityAddress = identityAddress;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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
