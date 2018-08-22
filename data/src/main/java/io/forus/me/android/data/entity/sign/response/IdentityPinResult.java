package io.forus.me.android.data.entity.sign.response;

import com.google.gson.annotations.SerializedName;

public class IdentityPinResult {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("auth_code")
    private String authCode;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
