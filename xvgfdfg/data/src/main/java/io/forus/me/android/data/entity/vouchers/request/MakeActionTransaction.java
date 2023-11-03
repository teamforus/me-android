package io.forus.me.android.data.entity.vouchers.request;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class MakeActionTransaction {



    @SerializedName("product_id")
    private Long product_id;

    //@SerializedName("organization_id")
   // private Long organizationId;

    @SerializedName("note")
    private String note;

    public MakeActionTransaction( Long product_id, String note) {

        this.product_id = product_id;
        this.note = note;
    }



    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
