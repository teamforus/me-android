package io.forus.me.android.domain.models.vouchers

import java.math.BigDecimal
import java.util.*

class Transaction {

    var id: String

    var organization: Organization?

    var amount: BigDecimal?

    var amount_locale: String?

    var amount_extra_cash: BigDecimal?

    var amount_extra_cash_locale: String?

    var createdAt: Date?

    var type: Type = Type.Payed

    var product: Product?

    var state: String?

    var fund: Fund?

    var note: String?

    constructor(
        id: String,
        organization: Organization?,
        amount: BigDecimal?,
        amount_locale: String?,
        amount_extra_cash: BigDecimal?,
        amount_extra_cash_locale: String?,
        createdAt: Date?,
        product: Product?,
        state: String?,
        fund: Fund?,
        note: String?
    ) {
        this.id = id
        this.organization = organization
        this.amount = amount
        this.amount_locale = amount_locale
        this.amount_extra_cash = amount_extra_cash
        this.amount_extra_cash_locale = amount_extra_cash_locale
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
