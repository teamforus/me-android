package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

public class Organization {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("logo")
    private Logo logo;

    @SerializedName("identity_address")
    private String identityAddress;

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

    public Organization() {}

    public Organization(Long id, String name, Logo logo, String identityAddress, String iban, String email, String phone, String kvk, String btw) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.identityAddress = identityAddress;
        this.iban = iban;
        this.email = email;
        this.phone = phone;
        this.kvk = kvk;
        this.btw = btw;
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

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public String getIdentityAddress() {
        return identityAddress;
    }

    public void setIdentityAddress(String identityAddress) {
        this.identityAddress = identityAddress;
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
}
