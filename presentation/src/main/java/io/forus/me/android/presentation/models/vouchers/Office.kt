package io.forus.me.android.presentation.models.vouchers

import android.os.Parcel
import android.os.Parcelable


class Office(var id: Long = -1L,
             var organizationId: Long? = -1L,
             var address: String? = "",
             var phone: String? = "",
             var lat: Double? = 0.0,
             var lon: Double? = 0.0,
             var photo: String? = "",
             var organization: Organization?,
             var schedulers: List<Schedule>) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readParcelable(Organization::class.java.classLoader) ?: Organization(),
            parcel.createTypedArrayList(Schedule)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(organizationId ?: -1L)
        parcel.writeString(address)
        parcel.writeString(phone)
        parcel.writeDouble(lat ?: 0.0)
        parcel.writeDouble(lon ?: 0.0)
        parcel.writeString(photo)
        parcel.writeParcelable(organization, flags)
        parcel.writeTypedList(schedulers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Office> {
        override fun createFromParcel(parcel: Parcel): Office {
            return Office(parcel)
        }

        override fun newArray(size: Int): Array<Office?> {
            return arrayOfNulls(size)
        }
    }

}