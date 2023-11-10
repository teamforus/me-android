package io.forus.me.android.data.entity.vouchers.request;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class MakeDemoTransaction {


    @SerializedName("state")
    private String state = "accepted";

    public MakeDemoTransaction(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
