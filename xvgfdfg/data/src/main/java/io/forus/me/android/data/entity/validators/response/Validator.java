package io.forus.me.android.data.entity.validators.response;

import com.google.gson.annotations.SerializedName;

public class Validator {

    @SerializedName("id")
    private Long id;

    @SerializedName("organization_id")
    private Long organizationid;

    @SerializedName("identity_address")
    private String identityAddress;

    @SerializedName("email")
    private String email;

    @SerializedName("organization")
    private Organization organization;

    public Validator() {}

    public Validator(Long id, Long organizationid, String identityAddress, String email, Organization organization) {
        this.id = id;
        this.organizationid = organizationid;
        this.identityAddress = identityAddress;
        this.email = email;
        this.organization = organization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationid() {
        return organizationid;
    }

    public void setOrganizationid(Long organizationid) {
        this.organizationid = organizationid;
    }

    public String getIdentityAddress() {
        return identityAddress;
    }

    public void setIdentityAddress(String identityAddress) {
        this.identityAddress = identityAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
