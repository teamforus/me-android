package io.forus.me.android.presentation.models.vouchers

import android.os.Parcel
import android.os.Parcelable

class Organization(var id: Long = -1L,
                   var name: String = "",
                   var logo: String = "",
                   var lat: Double = 0.0,
                   var lon: Double = 0.0,
                   var address: String = "",
                   var phone: String = "",
                   var email: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(logo)
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
        parcel.writeString(address)
        parcel.writeString(phone)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Organization> {
        override fun createFromParcel(parcel: Parcel): Organization {
            return Organization(parcel)
        }

        override fun newArray(size: Int): Array<Organization?> {
            return arrayOfNulls(size)
        }
    }

}