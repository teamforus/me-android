package io.forus.me.android.data.entity.common;

import com.google.gson.annotations.SerializedName;

public class Success {

    @SerializedName("success")
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}