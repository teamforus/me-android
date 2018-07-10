package io.forus.me.android.domain.models.wallets

import io.forus.me.android.domain.models.currency.Currency


class Wallet {


    constructor(name: String, balance: Float, currency: Currency?) {
        this.name = name
        this.balance = balance
        this.currency = currency
    }

    var name: String = ""

    var balance: Float = 0f

    var currency: Currency? = null


}
