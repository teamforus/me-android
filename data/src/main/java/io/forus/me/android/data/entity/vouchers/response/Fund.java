package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

public class Fund {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("state")
    private String state;

    @SerializedName("url_webshop")
    private String webShopUrl;

    @SerializedName("organization")
    private Organization organization;

    @SerializedName("logo")
    private Logo logo;

    @SerializedName("type")
    private String type;


    public Fund() { }

    public Fund(Long id, String name, String state, String webShopUrl, Organization organization, Logo logo, String type) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.webShopUrl = webShopUrl;
        this.organization = organization;
        this.logo = logo;
        this.type = type;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public String getWebShopUrl() {
        return webShopUrl;
    }

    public void setWebShopUrl(String webShopUrl) {
        this.webShopUrl = webShopUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
