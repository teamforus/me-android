package io.forus.me.android.data.entity.sign.response;

import com.google.gson.annotations.SerializedName;

public class ValidateEmailResult {

    @SerializedName("email")
    private ValidateEmail email;


    public ValidateEmail getEmail() {
        return email;
    }

    public void setEmail(ValidateEmail email) {
        this.email = email;
    }
}
