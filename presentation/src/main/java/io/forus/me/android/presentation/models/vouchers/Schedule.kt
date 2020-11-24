package io.forus.me.android.presentation.models.vouchers

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal

class Schedule(var id: Long = -1L,
               var officeId: Long = -1L,
               var weekDay: Long = -1L,
               var startTime: String?,
               var endTime: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(officeId)
        parcel.writeLong(weekDay)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Schedule> {
        override fun createFromParcel(parcel: Parcel): Schedule {
            return Schedule(parcel)
        }

        override fun newArray(size: Int): Array<Schedule?> {
            return arrayOfNulls(size)
        }
    }
}