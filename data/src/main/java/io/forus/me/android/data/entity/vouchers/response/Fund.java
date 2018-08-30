package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

public class Fund {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("state")
    private String state;

    @SerializedName("organization")
    private Organization organization;

    @SerializedName("logo")
    private Logo logo;

    public Fund() { }

    public Fund(Long id, String name, String state, Organization organization, Logo logo) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.organization = organization;
        this.logo = logo;
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
}
