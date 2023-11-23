package io.forus.me.android.domain.models.wallets

import io.forus.me.android.domain.models.currency.Currency
import java.util.*

class Transaction {

    var id: String

    var walletId: Long

    val timestamp: Date

    var title: String

    var receiver: String

    var currency: Currency

    var value: Double

    var type: Type

    var status: Status

    constructor(id: String, walletId: Long, timestamp: Date, title: String, receiver: String, currency: Currency, value: Double, type: Type, status: Status) {
        this.id = id
        this.walletId = walletId
        this.timestamp = timestamp
        this.title = title
        this.receiver = receiver
        this.currency = currency
        this.value = value
        this.type = type
        this.status = status
    }


    enum class Type {
        PAYMENT, INCOME
    }

    enum class Status {
        IN_PROGRESS, CONFIRMED, CANCELED
    }
}