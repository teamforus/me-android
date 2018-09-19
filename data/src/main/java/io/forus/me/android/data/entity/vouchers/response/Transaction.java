package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Transaction {

    @SerializedName("organization_id")
    private Long organizationId;

    @SerializedName("product_id")
    private Long productId;

    @SerializedName("amount")
    private BigDecimal amount;

    @SerializedName("address")
    private String address;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("date")
    private String date;

    @SerializedName("date_time")
    private String dateTime;

    @SerializedName("organization")
    private Organization organization;

    @SerializedName("product")
    private Product product;

    public Transaction() { }

    public Transaction(Long organizationId, Long productId, BigDecimal amount, String address, String createdAt, String updatedAt, String date, String dateTime, Organization organization, Product product) {
        this.organizationId = organizationId;
        this.productId = productId;
        this.amount = amount;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.date = date;
        this.dateTime = dateTime;
        this.organization = organization;
        this.product = product;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
