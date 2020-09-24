package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment;

import java.io.Serializable;

public class ProductSerializable implements Serializable {

    long id;
    String name;
    String companyName;
    double price;
    long companyId;


    public ProductSerializable(long id, String name,  String companyName, long companyId, double price ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.companyName = companyName;
        this.companyId = companyId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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
}
