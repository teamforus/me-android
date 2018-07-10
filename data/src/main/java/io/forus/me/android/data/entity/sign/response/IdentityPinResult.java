package io.forus.me.android.data.entity.sign.response;

import com.google.gson.annotations.SerializedName;

public class IdentityPinResult {

    @SerializedName("auth_code")
    private String authCode;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
