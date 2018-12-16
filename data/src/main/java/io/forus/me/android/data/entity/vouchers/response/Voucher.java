package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Voucher {

    public enum Type {
        regular, product
    }

    @SerializedName("fund_id")
    private Long fundId;

    @SerializedName("identity_address")
    private String identityAddress;

    @SerializedName("address")
    private String address;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("created_at_locale")
    private String createdAtLocale;

    @SerializedName("expire_at_locale")
    private String expireAtLocale;

    @SerializedName("timestamp")
    private Long timestamp;

    @SerializedName("type")
    private Type type;

    @SerializedName("product")
    private Product product;

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

    @SerializedName("product_vouchers")
    private List<Voucher> childVouchers;

    @SerializedName("offices")
    private List<Office> offices;

    public Voucher() { }

    public Voucher(Long fundId, String identityAddress, String address, Date createdAt, String createdAtLocale, String expireAtLocale, Long timestamp, Type type, Product product, BigDecimal amount, Fund fund, List<Transaction> transactions, List<Organization> allowedOrganizations, List<ProductCategory> allowedProductCategories, List<Product> allowedProducts, List<Voucher> childVouchers, List<Office> offices) {
        this.fundId = fundId;
        this.identityAddress = identityAddress;
        this.address = address;
        this.createdAt = createdAt;
        this.createdAtLocale = createdAtLocale;
        this.expireAtLocale = expireAtLocale;
        this.timestamp = timestamp;
        this.type = type;
        this.product = product;
        this.amount = amount;
        this.fund = fund;
        this.transactions = transactions;
        this.allowedOrganizations = allowedOrganizations;
        this.allowedProductCategories = allowedProductCategories;
        this.allowedProducts = allowedProducts;
        this.childVouchers = childVouchers;
        this.offices = offices;
    }

    public Long getFundId() {
        return fundId;
    }

    public void setFundId(Long fundId) {
        this.fundId = fundId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtLocale() {
        return createdAtLocale;
    }

    public void setCreatedAtLocale(String createdAtLocale) {
        this.createdAtLocale = createdAtLocale;
    }

    public String getExpireAtLocale() {
        return expireAtLocale;
    }

    public void setExpireAtLocale(String expireAtLocale) {
        this.expireAtLocale = expireAtLocale;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public List<Voucher> getChildVouchers() {
        return childVouchers;
    }

    public void setChildVouchers(List<Voucher> childVouchers) {
        this.childVouchers = childVouchers;
    }

    public List<Office> getOffices() {
        return offices;
    }

    public void setOffices(List<Office> offices) {
        this.offices = offices;
    }
}
