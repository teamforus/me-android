package io.forus.me.android.data.entity.vouchers.request;

import com.google.gson.annotations.SerializedName;

public class MakeTransaction {

    @SerializedName("amount")
    private Long amount;

    @SerializedName("organization_id")
    private Long organizationId;

    public MakeTransaction() {
    }

    public MakeTransaction(Long amount, Long organizationId) {
        this.amount = amount;
        this.organizationId = organizationId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
