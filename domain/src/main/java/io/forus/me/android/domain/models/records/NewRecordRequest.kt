package io.forus.me.android.domain.models.records

class NewRecordRequest {

    constructor(recordType: RecordType, category: RecordCategory, value: String, order: Long) {
        this.recordType = recordType
        this.category = category
        this.value = value
        this.order = order
    }

    var recordType: RecordType

    var category: RecordCategory

    var value: String

    var order: Long
}