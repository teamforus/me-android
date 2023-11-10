package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListAllTransactions {

    @SerializedName("data")
    private List<Transaction> data;

    public ListAllTransactions() { }

    public ListAllTransactions(List<Transaction> data) {
        this.data = data;
    }

    public List<Transaction> getData() {
        return data;
    }

    public void setData(List<Transaction> data) {
        this.data = data;
    }
}
