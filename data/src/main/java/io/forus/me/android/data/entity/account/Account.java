package io.forus.me.android.data.entity.account;

import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
