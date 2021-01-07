package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductSerializable implements Serializable {

    long id;
    String name;
    String companyName;
    BigDecimal price;

    String priceType;
    BigDecimal priceDiscount;
    BigDecimal sponsorSubsidy;

    BigDecimal priceUser;
    long companyId;

    String photoURL;
    String sponsorName;


    public ProductSerializable(long id, String name, String companyName,
                               long companyId,
                               BigDecimal price, BigDecimal priceUser,
                               String priceType,  BigDecimal priceDiscount,
                               BigDecimal sponsorSubsidy, String sponsorName,
                                String photoURL) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.companyName = companyName;
        this.companyId = companyId;
        this.sponsorSubsidy = sponsorSubsidy;
        this.priceType = priceType;
        this.priceDiscount = priceDiscount;
        this.priceUser = priceUser;
        this.photoURL = photoURL;
        this.sponsorName = sponsorName;

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

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(BigDecimal priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public BigDecimal getSponsorSubsidy() {
        return sponsorSubsidy;
    }

    public void setSponsorSubsidy(BigDecimal sponsorSubsidy) {
        this.sponsorSubsidy = sponsorSubsidy;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }
}
