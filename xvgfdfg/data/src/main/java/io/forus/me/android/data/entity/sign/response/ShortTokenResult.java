package io.forus.me.android.data.entity.sign.response;

import com.google.gson.annotations.SerializedName;

public class ShortTokenResult {

    @SerializedName("exchange_token")
    private String exchangeToken;

    public String getExchangeToken() {
        return exchangeToken;
    }

    public void setExchangeToken(String exchangeToken) {
        this.exchangeToken = exchangeToken;
    }
}
