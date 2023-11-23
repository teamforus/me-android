package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListAllVouchers {

    @SerializedName("data")
    private List<Voucher> data;

    public ListAllVouchers() { }

    public ListAllVouchers(List<Voucher> data) {
        this.data = data;
    }

    public List<Voucher> getData() {
        return data;
    }

    public void setData(List<Voucher> data) {
        this.data = data;
    }
}
