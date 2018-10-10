package io.forus.me.android.domain.models.vouchers

import io.forus.me.android.domain.models.currency.Currency
import java.math.BigDecimal
import java.util.*

class Transaction {

    var id: String

    var organization: Organization

    var currency: Currency

    var amount: BigDecimal

    var dateTime: String

    var type: Type = Type.Payed

    constructor(id: String, organization: Organization, currency: Currency, amount: BigDecimal, dateTime: String, type: Type = Type.Payed) {
        this.id = id
        this.organization = organization
        this.currency = currency
        this.amount = amount
        this.dateTime = dateTime
        this.type = type
    }


    enum class Type {
        Payed, Refund, Cancel, Income
    }
}
