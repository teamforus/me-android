package io.forus.me.android.data.entity.common;

import com.google.gson.annotations.SerializedName;

public class BaseError {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
