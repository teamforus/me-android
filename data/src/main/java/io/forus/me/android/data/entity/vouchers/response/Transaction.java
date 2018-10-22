package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

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
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    @SerializedName("date")
    private String date;

    @SerializedName("timestamp")
    private Long timestamp;

    @SerializedName("organization")
    private Organization organization;

    @SerializedName("product")
    private Product product;

    public Transaction() { }

    public Transaction(Long organizationId, Long productId, BigDecimal amount, String address, Date createdAt, Date updatedAt, String date, Long timestamp, Organization organization, Product product) {
        this.organizationId = organizationId;
        this.productId = productId;
        this.amount = amount;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.date = date;
        this.timestamp = timestamp;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
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
