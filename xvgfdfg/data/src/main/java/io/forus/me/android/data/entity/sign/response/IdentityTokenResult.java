package io.forus.me.android.data.entity.sign.response;

import com.google.gson.annotations.SerializedName;

public class IdentityTokenResult {

    @SerializedName("access_token")
    private String accessToken;


    @SerializedName("auth_token")
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
