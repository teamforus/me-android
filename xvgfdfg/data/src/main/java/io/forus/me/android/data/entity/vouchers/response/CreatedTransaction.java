package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

public class CreatedTransaction {

    @SerializedName("data")
    private Transaction data;

    public CreatedTransaction() { }

    public CreatedTransaction(Transaction data) {
        this.data = data;
    }

    public Transaction getData() {
        return data;
    }

    public void setData(Transaction data) {
        this.data = data;
    }
}
