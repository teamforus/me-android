package io.forus.me.android.domain.models.vouchers

import io.forus.me.android.domain.models.currency.Currency
import java.math.BigDecimal
import java.util.*

class Transaction {

    var id: String

    var organization: Organization?

    var currency: Currency?

    var amount: BigDecimal?

    var amount_extra_cash: BigDecimal?

    var createdAt: Date?

    var type: Type = Type.Payed

    var product: Product?

    var state: String?

    var fund: Fund?

    var note : String?

    constructor(id: String, organization: Organization?, currency: Currency?, amount: BigDecimal?,amount_extra_cash: BigDecimal?, createdAt: Date?,
                product: Product?, state: String?, fund: Fund?, note: String?) {
        this.id = id
        this.organization = organization
        this.currency = currency
        this.amount = amount
        this.amount_extra_cash = amount_extra_cash
        this.createdAt = createdAt
        this.type = type
        this.product = product
        this.state = state
        this.fund = fund
        this.note = note
    }


    enum class Type {
        Payed, Refund, Cancel, Income, Product
    }
}
