package io.forus.me.android.domain.models.records

import io.forus.me.android.domain.models.currency.Currency
import java.util.*

class RecordType {



    var id: String = ""

    var name: String = ""


    var logo: String = ""


    constructor(id: String, name: String, logo: String) {
        this.id = id
        this.name = name
        this.logo = logo
    }
}
