package io.forus.me.android.domain.models.records

class NewRecordRequest {



    constructor(title: String? = "", value: String? = "", validated: Boolean? = false, category: RecordCategory? = null) {
        this.title = title
        this.value = value
        this.validated = validated
        this.category = category
    }

    constructor()

    var title: String? = null

    var value: String? = null

    var validated: Boolean? = null

    var category: RecordCategory? = null
}