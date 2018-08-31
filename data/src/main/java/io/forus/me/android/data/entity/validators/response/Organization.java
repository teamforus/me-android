package io.forus.me.android.data.entity.validators.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Organization {

    @SerializedName("id")
    private Long id;

    @SerializedName("identity_address")
    private String identityAddress;

    @SerializedName("name")
    private String name;

    @SerializedName("iban")
    private String iban;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("kvk")
    private String kvk;

    @SerializedName("btw")
    private String btw;

    @SerializedName("logo")
    private Logo logo;

    @SerializedName("product_categories")
    private List<ProductCategory> productCategories;

    public Organization() { }

    public Organization(Long id, String identityAddress, String name, String iban, String email, String phone, String kvk, String btw, Logo logo, List<ProductCategory> productCategories) {
        this.id = id;
        this.identityAddress = identityAddress;
        this.name = name;
        this.iban = iban;
        this.email = email;
        this.phone = phone;
        this.kvk = kvk;
        this.btw = btw;
        this.logo = logo;
        this.productCategories = productCategories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentityAddress() {
        return identityAddress;
    }

    public void setIdentityAddress(String identityAddress) {
        this.identityAddress = identityAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getKvk() {
        return kvk;
    }

    public void setKvk(String kvk) {
        this.kvk = kvk;
    }

    public String getBtw() {
        return btw;
    }

    public void setBtw(String btw) {
        this.btw = btw;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }
}
