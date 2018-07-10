package io.forus.me.android.domain.models.vouchers

import io.forus.me.android.domain.models.currency.Currency
import java.util.*

class Transaction {

    var id: String = ""

    var title: String = ""

    var type: Type = Type.Payed

    var currency: Currency = Currency()

    var value: Double  = 0f.toDouble()

    constructor(id: String, title: String, type: Type, currency: Currency, value: Double) {
        this.id = id
        this.title = title
        this.type = type
        this.currency = currency
        this.value = value
    }


    enum class Type {
        Payed, Refund, Cancel, Income
    }
}
