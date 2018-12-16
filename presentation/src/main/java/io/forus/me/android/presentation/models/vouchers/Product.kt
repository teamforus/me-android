package io.forus.me.android.presentation.models.vouchers

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal

class Product(var id: Long,
              var organizationId: Long,
              var productCategoryId: Long,
              var name: String,
              var description: String,
              var price: BigDecimal,
              var oldPrice: BigDecimal,
              var totalAmount: Long,
              var soldAmount: Long,
              var productCategory: ProductCategory,
              var organization: Organization) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            BigDecimal(parcel.readDouble()),
            BigDecimal(parcel.readDouble()),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readParcelable(ProductCategory::class.java.classLoader),
            parcel.readParcelable(Organization::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(organizationId)
        parcel.writeLong(productCategoryId)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeDouble(price.toDouble())
        parcel.writeDouble(oldPrice.toDouble())
        parcel.writeLong(totalAmount)
        parcel.writeLong(soldAmount)
        parcel.writeParcelable(productCategory, flags)
        parcel.writeParcelable(organization, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}