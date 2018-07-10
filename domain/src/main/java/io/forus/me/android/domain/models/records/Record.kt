package io.forus.me.android.domain.models.records

import com.sun.org.apache.xpath.internal.operations.Bool
import io.forus.me.android.domain.models.currency.Currency
import java.util.*

class Record {


    var id: String = ""

    var title: String = ""

    var value: String = ""

    val validated: Boolean = false

    lateinit var type: RecordType

    constructor(id: String, title: String, value: String, type: RecordType) {
        this.id = id
        this.title = title
        this.value = value
        this.type = type
    }
}
