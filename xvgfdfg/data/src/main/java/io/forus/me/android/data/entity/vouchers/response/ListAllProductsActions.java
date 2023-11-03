package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListAllProductsActions {

    @SerializedName("data")
    private List<ProductAction> data;

    public ListAllProductsActions() { }

    public ListAllProductsActions(List<ProductAction> data) {
        this.data = data;
    }

    public List<ProductAction> getData() {
        return data;
    }

    public void setData(List<ProductAction> data) {
        this.data = data;
    }
}
