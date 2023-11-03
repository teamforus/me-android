package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

public class GetVoucher {

    @SerializedName("data")
    private Voucher data;

    public GetVoucher() { }

    public GetVoucher(Voucher data) {
        this.data = data;
    }

    public Voucher getData() {
        return data;
    }

    public void setData(Voucher data) {
        this.data = data;
    }
}
