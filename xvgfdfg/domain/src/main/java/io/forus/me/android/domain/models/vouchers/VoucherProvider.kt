package io.forus.me.android.domain.models.vouchers

class VoucherProvider {

    var voucher: Voucher

    var allowedOrganizations: List<Organization>

    var allowedProductCategories: List<ProductCategory>

    constructor(voucher: Voucher, allowedOrganizations: List<Organization>, allowedProductCategories: List<ProductCategory>) {
        this.voucher = voucher
        this.allowedOrganizations = allowedOrganizations
        this.allowedProductCategories = allowedProductCategories
    }
}