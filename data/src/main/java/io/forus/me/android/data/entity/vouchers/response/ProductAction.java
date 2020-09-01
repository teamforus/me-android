package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

import io.forus.me.android.data.entity.vouchers.response.Logo;
import io.forus.me.android.data.entity.vouchers.response.Organization;
import io.forus.me.android.data.entity.vouchers.response.ProductCategory;

public class ProductAction {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("organization_id")
    private long organizationId;

   // @SerializedName("expired")
   // private boolean expired;

    //@SerializedName("sold_out")
    //private boolean soldOut;


    @SerializedName("price")
    private BigDecimal price;

    @SerializedName("price_user")
    private BigDecimal priceUser;

    //@SerializedName("price_old")
    //private BigDecimal priceOld;

    //@SerializedName("expire_at")
    //private String expireAt;

   // @SerializedName("expire_at_locale")
   // private String expireAtLocale;

    @SerializedName("photo")
    private Logo photo;


    @SerializedName("organization")
    private Organization organization;

    @SerializedName("product_category")
    private ProductCategory productCategory;


    public ProductAction() {
    }

    public ProductAction(long id, String name, long organizationId, BigDecimal price, BigDecimal priceUser, Logo photo, Organization organization, ProductCategory productCategory) {
        this.id = id;
        this.name = name;
        this.organizationId = organizationId;
        this.price = price;
        this.priceUser = priceUser;
        this.photo = photo;
        this.organization = organization;
        this.productCategory = productCategory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceUser() {
        return priceUser;
    }

    public void setPriceUser(BigDecimal priceUser) {
        this.priceUser = priceUser;
    }

    public Logo getPhoto() {
        return photo;
    }

    public void setPhoto(Logo photo) {
        this.photo = photo;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }
}
