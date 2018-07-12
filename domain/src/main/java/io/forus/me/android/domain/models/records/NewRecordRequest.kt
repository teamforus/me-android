package io.forus.me.android.domain.models.records

class NewRecordRequest {



    constructor(recordType: RecordType? = null, category: RecordCategory? = null, value: String? = "", order: Long? = 0) {
        this.recordType = recordType;
        this.category = category
        this.value = value
        this.order = order
    }

    constructor()

    var recordType: RecordType? = null

    var category: RecordCategory? = null

    var value: String? = null

    var order: Long? = null
}