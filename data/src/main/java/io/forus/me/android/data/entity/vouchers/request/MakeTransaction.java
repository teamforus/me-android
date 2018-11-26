package io.forus.me.android.data.entity.vouchers.request;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class MakeTransaction {

    @SerializedName("amount")
    private BigDecimal amount;

    @SerializedName("note")
    private String note;

    @SerializedName("organization_id")
    private Long organizationId;

    public MakeTransaction() {
    }

    public MakeTransaction(BigDecimal amount, String note, Long organizationId) {
        this.amount = amount;
        this.note = note;
        this.organizationId = organizationId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
