package io.forus.me.android.data.entity.sign.response;

import com.google.gson.annotations.SerializedName;

public class AccessToken {

    @SerializedName("success")
    private boolean success;


    @SerializedName("access_token")
    private String accessToken;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
