package io.forus.me.android.domain.models.wallets

import io.forus.me.android.domain.models.currency.Currency


class Wallet {


    constructor(name: String, balance: Float, currency: Currency?, url: String?) {
        this.name = name
        this.balance = balance
        this.currency = currency
        this.logoUrl = url
    }

    var name: String = ""

    var logoUrl: String? = ""

    var balance: Float = 0f

    var currency: Currency? = null


}
