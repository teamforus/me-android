package io.forus.me.android.data.entity.vouchers.request;

import com.google.gson.annotations.SerializedName;

public class MakeTransaction {

    @SerializedName("amount")
    private Float amount;

    @SerializedName("organization_id")
    private Long organizationId;

    public MakeTransaction() {
    }

    public MakeTransaction(Float amount, Long organizationId) {
        this.amount = amount;
        this.organizationId = organizationId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
