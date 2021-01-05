package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductSerializable implements Serializable {

    long id;
    String name;
    String companyName;
    BigDecimal price;
    BigDecimal oldPrice;
    boolean noPrice;
    String noPriceType;
    BigDecimal noPriceDiscount;

    BigDecimal priceUser;
    long companyId;

    String photoURL;


    public ProductSerializable(long id, String name, String companyName, long companyId, BigDecimal price,
                               BigDecimal oldPrice,
                               boolean noPrice, String noPriceType, BigDecimal noPriceDiscount,
                               BigDecimal priceUser, String photoURL) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.companyName = companyName;
        this.companyId = companyId;
        this.oldPrice = oldPrice;
        this.noPrice = noPrice;
        this.noPriceType = noPriceType;
        this.noPriceDiscount = noPriceDiscount;
        this.priceUser = priceUser;
        this.photoURL = photoURL;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public BigDecimal getPriceUser() {
        return priceUser;
    }

    public void setPriceUser(BigDecimal priceUser) {
        this.priceUser = priceUser;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public boolean isNoPrice() {
        return noPrice;
    }

    public void setNoPrice(boolean noPrice) {
        this.noPrice = noPrice;
    }

    public String getNoPriceType() {
        return noPriceType;
    }

    public void setNoPriceType(String noPriceType) {
        this.noPriceType = noPriceType;
    }

    public BigDecimal getNoPriceDiscount() {
        return noPriceDiscount;
    }

    public void setNoPriceDiscount(BigDecimal noPriceDiscount) {
        this.noPriceDiscount = noPriceDiscount;
    }
}
