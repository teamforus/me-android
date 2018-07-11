package io.forus.me.android.domain.models.records

class Record {


    var id: String = ""

    var title: String = ""

    var value: String = ""

    val validated: Boolean = false

    var category: RecordCategory

    constructor(id: String, title: String, value: String, category: RecordCategory) {
        this.id = id
        this.title = title
        this.value = value
        this.category = category
    }
}
