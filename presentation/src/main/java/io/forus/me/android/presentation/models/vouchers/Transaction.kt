package io.forus.me.android.presentation.models.vouchers

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal
import java.util.Date

class Transaction(
    var id: String,
    var organization: Organization?,
    var amount: BigDecimal,
    var amount_locale: String? = null,
    var amount_extra_cash: BigDecimal,
    var amount_extra_cash_locale: String? = null,
    var createdAt: Date?,
    var type: Type = Type.Payed,
    var product: Product?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readParcelable(Organization::class.java.classLoader) ?: Organization(),
        BigDecimal.valueOf(parcel.readDouble()),
        parcel.readString(),
        BigDecimal.valueOf(parcel.readDouble()),
        parcel.readString(),
        Date(parcel.readLong()),
        Type.valueOf(parcel.readString() ?: "Payed"),
        parcel.readParcelable(Product::class.java.classLoader) ?: null
    )

    enum class Type {
        Payed, Refund, Cancel, Income, Product
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeParcelable(organization, flags)
        parcel.writeDouble(amount.toDouble())
        parcel.writeString(amount_locale)
        parcel.writeDouble(amount_extra_cash.toDouble())
        parcel.writeString(amount_extra_cash_locale)
        parcel.writeLong(createdAt?.time ?: 0)
        parcel.writeString(type.name)
        parcel.writeParcelable(product, flags)
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
