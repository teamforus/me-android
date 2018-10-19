package io.forus.me.android.domain.models.vouchers

import io.forus.me.android.domain.models.currency.Currency
import java.math.BigDecimal
import java.util.*

class Voucher {

    var isProduct: Boolean

    var isUsed: Boolean

    var address: String

    var name: String

    var organizationName: String

    var createdAt: Date

    var currency: Currency

    var amount: BigDecimal

    var logo: String

    var transactions: List<Transaction>

    constructor(isProduct: Boolean, isUsed: Boolean, address: String, name: String, organizationName: String, createdAt: Date, currency: Currency, amount: BigDecimal, logo: String, transactions: List<Transaction>) {
        this.isProduct = isProduct
        this.isUsed = isUsed
        this.address = address
        this.name = name
        this.organizationName = organizationName
        this.createdAt = createdAt
        this.currency = currency
        this.amount = amount
        this.logo = logo
        this.transactions = transactions
    }


//    fun getValidString() : String {
//        return "Nog $validDays dagen geldig Valid"
//    }
}
