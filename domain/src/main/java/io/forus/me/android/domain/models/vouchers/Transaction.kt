package io.forus.me.android.domain.models.vouchers

import io.forus.me.android.domain.models.currency.Currency
import java.math.BigDecimal
import java.util.*

class Transaction {

    var id: String

    var organization: Organization?

    var currency: Currency?

    var amount: BigDecimal?

    var createdAt: Date?

    var type: Type = Type.Payed

    constructor(id: String, organization: Organization?, currency: Currency?, amount: BigDecimal?, createdAt: Date?, type: Type = Type.Payed) {
        this.id = id
        this.organization = organization
        this.currency = currency
        this.amount = amount
        this.createdAt = createdAt
        this.type = type
    }


    enum class Type {
        Payed, Refund, Cancel, Income, Product
    }
}
