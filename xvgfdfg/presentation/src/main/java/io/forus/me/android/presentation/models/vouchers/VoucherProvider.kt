package io.forus.me.android.presentation.models.vouchers

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class VoucherProvider(var voucher: Voucher, var allowedOrganizations: List<Organization>, var allowedProductCategories: List<ProductCategory>) : Parcelable {
   /* constructor(parcel: Parcel) : this(
        parcel.readParcelable(Voucher::class.java.classLoader) as Voucher,
            parcel.createTypedArrayList(Organization)?: listOf<Organization>(),
            parcel.createTypedArrayList(ProductCategory)?: listOf<ProductCategory>()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(voucher, flags)
        parcel.writeTypedList(allowedOrganizations)
        parcel.writeTypedList(allowedProductCategories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VoucherProvider> {
        override fun createFromParcel(parcel: Parcel): VoucherProvider {
            return VoucherProvider(parcel)
        }

        override fun newArray(size: Int): Array<VoucherProvider?> {
            return arrayOfNulls(size)
        }
    }*/

}