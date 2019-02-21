package io.forus.me.android.presentation.models.vouchers

import android.os.Parcel
import android.os.Parcelable
import io.forus.me.android.presentation.models.currency.Currency
import java.math.BigDecimal
import java.util.*

class Transaction(var id: String, var organization: Organization?, var currency: Currency?, var amount: BigDecimal, var createdAt: Date?, var type: Type = Type.Payed) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readParcelable(Organization::class.java.classLoader)?: Organization(),
            parcel.readParcelable(Currency::class.java.classLoader) ?: Currency(),
            BigDecimal.valueOf(parcel.readDouble()),
            Date(parcel.readLong()),
            Type.valueOf(parcel.readString() ?: "Payed"))

    enum class Type {
        Payed, Refund, Cancel, Income, Product
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeParcelable(organization, flags)
        parcel.writeParcelable(currency, flags)
        parcel.writeDouble(amount.toDouble())
        parcel.writeLong(createdAt?.time ?: 0)
        parcel.writeString(type.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }

}
