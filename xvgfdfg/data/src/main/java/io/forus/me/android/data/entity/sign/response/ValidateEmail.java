package io.forus.me.android.data.entity.sign.response;

import com.google.gson.annotations.SerializedName;

public class ValidateEmail {

    @SerializedName("used")
    private Boolean used;

    @SerializedName("valid")
    private Boolean valid;

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
