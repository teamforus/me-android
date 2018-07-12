package io.forus.me.android.data.entity.common;

import com.google.gson.annotations.SerializedName;

public class Success {

    @SerializedName("success")
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}