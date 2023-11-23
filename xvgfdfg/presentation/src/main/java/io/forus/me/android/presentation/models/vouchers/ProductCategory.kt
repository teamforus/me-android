package io.forus.me.android.presentation.models.vouchers

import android.os.Parcel
import android.os.Parcelable

class ProductCategory(var id: Long, var key: String?, var name: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(key)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductCategory> {
        override fun createFromParcel(parcel: Parcel): ProductCategory {
            return ProductCategory(parcel)
        }

        override fun newArray(size: Int): Array<ProductCategory?> {
            return arrayOfNulls(size)
        }
    }

}