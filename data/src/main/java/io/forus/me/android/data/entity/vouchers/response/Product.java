package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Product {

    @SerializedName("id")
    private Long id;

    @SerializedName("organization_id")
    private Long organizationId;

    @SerializedName("product_category_id")
    private Long productCategoryId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private BigDecimal price;

    @SerializedName("old_price")
    private BigDecimal oldPrice;

    @SerializedName("total_amount")
    private Long totalAmount;

    @SerializedName("sold_amount")
    private Long soldAmount;

    @SerializedName("photo")
    private Logo photo;

    @SerializedName("product_category")
    private ProductCategory productCategory;

    @SerializedName("organization")
    private Organization organization;

    public Product() { }

    public Product(Long id, Long organizationId, Long productCategoryId, String name, String description, BigDecimal price, BigDecimal oldPrice, Long totalAmount, Long soldAmount, Logo photo, ProductCategory productCategory, Organization organization) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
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

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(Long soldAmount) {
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
