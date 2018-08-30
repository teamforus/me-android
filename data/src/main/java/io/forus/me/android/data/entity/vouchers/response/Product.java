package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private Long price;

    @SerializedName("old_price")
    private Long oldPrice;

    @SerializedName("total_amount")
    private Long totalAmount;

    @SerializedName("sold_amount")
    private Long soldAmount;

    @SerializedName("photo")
    private Logo photo;

    @SerializedName("product_category")
    private ProductCategory productCategory;

    public Product() { }

    public Product(Long id, String name, String description, Long price, Long oldPrice, Long totalAmount, Long soldAmount, Logo photo, ProductCategory productCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.oldPrice = oldPrice;
        this.totalAmount = totalAmount;
        this.soldAmount = soldAmount;
        this.photo = photo;
        this.productCategory = productCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Long oldPrice) {
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
}
