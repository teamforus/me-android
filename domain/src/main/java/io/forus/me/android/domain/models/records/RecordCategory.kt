package io.forus.me.android.domain.models.records

import io.forus.me.android.domain.models.currency.Currency
import java.util.*

class RecordCategory {



    var id: Long = 0

    var name: String = ""


    var logo: String = ""

    var order: Long = 0


    constructor(id: Long, name: String, order: Long, logo: String = "https://www.freelogodesign.org/Content/img/logo-ex-7.png") {
        this.id = id
        this.name = name
        this.order = order
        this.logo = logo
    }
}
