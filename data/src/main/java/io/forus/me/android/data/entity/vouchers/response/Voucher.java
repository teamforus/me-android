package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class Voucher {

    @SerializedName("fund_id")
    private Long data;

    @SerializedName("identity_address")
    private String identityAddress;

    @SerializedName("address")
    private String address;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("amount")
    private BigDecimal amount;

    @SerializedName("fund")
    private Fund fund;

    @SerializedName("transactions")
    private List<Transaction> transactions;

    @SerializedName("allowed_organizations")
    private List<Organization> allowedOrganizations;

    @SerializedName("allowed_product_categories")
    private List<ProductCategory> allowedProductCategories;

    @SerializedName("allowed_products")
    private List<Product> allowedProducts;

    public Voucher() { }

    public Voucher(Long data, String identityAddress, String address, String createdAt, BigDecimal amount, Fund fund, List<Transaction> transactions, List<Organization> allowedOrganizations, List<ProductCategory> allowedProductCategories, List<Product> allowedProducts) {
        this.data = data;
        this.identityAddress = identityAddress;
        this.address = address;
        this.createdAt = createdAt;
        this.amount = amount;
        this.fund = fund;
        this.transactions = transactions;
        this.allowedOrganizations = allowedOrganizations;
        this.allowedProductCategories = allowedProductCategories;
        this.allowedProducts = allowedProducts;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

    public String getIdentityAddress() {
        return identityAddress;
    }

    public void setIdentityAddress(String identityAddress) {
        this.identityAddress = identityAddress;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Organization> getAllowedOrganizations() {
        return allowedOrganizations;
    }

    public void setAllowedOrganizations(List<Organization> allowedOrganizations) {
        this.allowedOrganizations = allowedOrganizations;
    }

    public List<ProductCategory> getAllowedProductCategories() {
        return allowedProductCategories;
    }

    public void setAllowedProductCategories(List<ProductCategory> allowedProductCategories) {
        this.allowedProductCategories = allowedProductCategories;
    }

    public List<Product> getAllowedProducts() {
        return allowedProducts;
    }

    public void setAllowedProducts(List<Product> allowedProducts) {
        this.allowedProducts = allowedProducts;
    }
}
