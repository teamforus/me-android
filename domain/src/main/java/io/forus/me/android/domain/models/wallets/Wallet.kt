package io.forus.me.android.domain.models.wallets

import io.forus.me.android.domain.models.currency.Currency


class Wallet {


    constructor(id: Long, name: String, address: String, balance: Float, currency: Currency?, url: String?) {
        this.id = id
        this.name = name
        this.address = address
        this.balance = balance
        this.currency = currency
        this.logoUrl = url
    }

    var id: Long = 0

    var name: String = ""

    var address: String

    var logoUrl: String? = ""

    var balance: Float = 0f

    var currency: Currency? = null

}
