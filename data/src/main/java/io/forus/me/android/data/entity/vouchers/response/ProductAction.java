package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductAction implements Serializable {

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


    @SerializedName("sponsor_subsidy")
    private BigDecimal sponsorSubsidy;



    @SerializedName("price_type")
    private String price_type;


    @SerializedName("price_discount")
    private BigDecimal price_discount;

    @SerializedName("price_user_locale")
    private String priceUserLocale;



    //@SerializedName("expire_at")
    //private String expireAt;

   // @SerializedName("expire_at_locale")
   // private String expireAtLocale;

    @SerializedName("photo")
    private Logo photo;


    @SerializedName("organization")
    private Organization organization;

    @SerializedName("sponsor")
    private Organization sponsor;

    @SerializedName("product_category")
    private ProductCategory productCategory;


    public ProductAction() {
    }

    public ProductAction(long id, String name, long organizationId,
                         BigDecimal price, BigDecimal priceUser,
                         String priceType, BigDecimal priceDiscount,
                         String price_user_locale,
                         BigDecimal sponsorSubsidy,Logo photo,
                         Organization organization, Organization sponsor,
                         ProductCategory productCategory) {
        this.id = id;
        this.name = name;
        this.organizationId = organizationId;
        this.price = price;
        this.priceUser = priceUser;
        this.photo = photo;
        this.organization = organization;
        this.sponsor = sponsor;
        this.productCategory = productCategory;
        this.sponsorSubsidy = sponsorSubsidy;
        this.price_type = priceType;
        this.price_discount = priceDiscount;
        this.priceUserLocale = price_user_locale;
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

    public BigDecimal getSponsorSubsidy() {
        return sponsorSubsidy;
    }

    public void setSponsorSubsidy(BigDecimal sponsorSubsidy) {
        this.sponsorSubsidy = sponsorSubsidy;
    }

    public Organization getSponsor() {
        return sponsor;
    }

    public void setSponsor(Organization sponsor) {
        this.sponsor = sponsor;
    }

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public BigDecimal getPrice_discount() {
        return price_discount;
    }

    public void setPrice_discount(BigDecimal price_discount) {
        this.price_discount = price_discount;
    }

    public String  getPriceUserLocale() {
        return priceUserLocale;
    }

    public void setPriceUserLocale(String priceUserLocale) {
        this.priceUserLocale = priceUserLocale;
    }
}
