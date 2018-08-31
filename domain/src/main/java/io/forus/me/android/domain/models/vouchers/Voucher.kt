package io.forus.me.android.domain.models.vouchers

import io.forus.me.android.domain.models.currency.Currency
import java.math.BigDecimal
import java.util.*

class Voucher {

    var address: String

    var name: String

    var validDays: Int

    var currency: Currency

    var amount: BigDecimal

    var logo: String

    var transactions: List<Transaction>

    constructor(address: String, name: String, validDays: Int, currency: Currency, amount: BigDecimal, logo: String, transactions: List<Transaction> = emptyList()) {
        this.address = address
        this.name = name
        this.validDays = validDays
        this.currency = currency
        this.amount = amount
        this.logo = logo
        this.transactions = transactions
    }


    fun getValidString() : String {
        return "Nog $validDays dagen geldig Valid"
    }
}
