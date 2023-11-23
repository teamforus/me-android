package io.forus.me.android.presentation.models.currency

import android.os.Parcel
import android.os.Parcelable

class Currency(var name: String? = "", var logoUrl: String? = "") : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()  ?: "", parcel.readString()  ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(logoUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Currency> {
        override fun createFromParcel(parcel: Parcel): Currency {
            return Currency(parcel)
        }

        override fun newArray(size: Int): Array<Currency?> {
            return arrayOfNulls(size)
        }
    }


}
