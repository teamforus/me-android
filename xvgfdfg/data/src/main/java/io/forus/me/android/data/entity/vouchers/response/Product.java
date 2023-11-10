package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Product {

    @SerializedName("id")
    private long id;

    @SerializedName("organization_id")
    private long organizationId;

    @SerializedName("product_category_id")
    private long productCategoryId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private BigDecimal price;

    @SerializedName("old_price")
    private BigDecimal oldPrice;

    @SerializedName("total_amount")
    private long totalAmount;

    @SerializedName("sold_amount")
    private long soldAmount;

    @SerializedName("photo")
    private Logo photo;

    @SerializedName("product_category")
    private ProductCategory productCategory;

    @SerializedName("organization")
    private Organization organization;

    public Product() { }

    public Product(long id, long organizationId, long productCategoryId, String name, String description, BigDecimal price, BigDecimal oldPrice, long totalAmount, long soldAmount, Logo photo, ProductCategory productCategory, Organization organization) {
        this.id = id;
        this.organizationId = organizationId;
        this.productCategoryId = productCategoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.oldPrice = oldPrice;
        this.totalAmount = totalAmount;
        this.soldAmount = soldAmount;
        this.photo = photo;
        this.productCategory = productCategory;
        this.organization = organization;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    public long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(long soldAmount) {
        this.soldAmount = soldAmount;
    }

    public Logo getPhoto() {
        return photo;
    }

    public void setPhoto(Logo photo) {
        this.photo = photo;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
