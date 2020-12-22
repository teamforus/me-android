package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

import io.forus.me.android.data.entity.vouchers.response.Logo;
import io.forus.me.android.data.entity.vouchers.response.Organization;
import io.forus.me.android.data.entity.vouchers.response.ProductCategory;

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

    @SerializedName("price_old")
    private BigDecimal price_old;


    @SerializedName("no_price")
    private boolean no_price;

    @SerializedName("no_price_type")
    private String no_price_type;


    @SerializedName("no_price_discount")
    private BigDecimal no_price_discount;



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

    public ProductAction(long id, String name, long organizationId, BigDecimal price, BigDecimal priceUser,BigDecimal priceOld, boolean noPrice,
                         String noPriceType, BigDecimal noPriceDiscount, Logo photo,  Organization organization, ProductCategory productCategory) {
        this.id = id;
        this.name = name;
        this.organizationId = organizationId;
        this.price = price;
        this.priceUser = priceUser;
        this.photo = photo;
        this.organization = organization;
        this.productCategory = productCategory;

        this.price_old = priceOld;
        this.no_price = noPrice;
        this.no_price_type = noPriceType;
        this.no_price_discount = noPriceDiscount;
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

    public BigDecimal getPrice_old() {
        return price_old;
    }

    public void setPrice_old(BigDecimal price_old) {
        this.price_old = price_old;
    }

    public boolean isNo_price() {
        return no_price;
    }

    public void setNo_price(boolean no_price) {
        this.no_price = no_price;
    }

    public String getNo_price_type() {
        return no_price_type;
    }

    public void setNo_price_type(String no_price_type) {
        this.no_price_type = no_price_type;
    }

    public BigDecimal getNo_price_discount() {
        return no_price_discount;
    }

    public void setNo_price_discount(BigDecimal no_price_discount) {
        this.no_price_discount = no_price_discount;
    }
}
