package io.forus.me.android.domain.models.vouchers

import io.forus.me.android.domain.models.currency.Currency
import java.util.*

class Voucher {



    var id: String = ""

    var name: String = ""

    var validDays: Int = 0

    var currency: io.forus.me.android.domain.models.currency.Currency

    var value: Float = 0f

    var logo: String = ""

    constructor(id: String, name: String, validDays: Int, currency: Currency, value: Float, logo: String) {
        this.id = id
        this.name = name
        this.validDays = validDays
        this.currency = currency
        this.value = value
        this.logo = logo
    }


    fun getValidString() : String {
        return "Valid til ${validDays} days"
    }
}
