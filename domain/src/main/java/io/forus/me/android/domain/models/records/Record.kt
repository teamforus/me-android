package io.forus.me.android.domain.models.records

class Record{

    var id: Long = 0

    var value: String = ""

    var name: String = ""

    var order: Long = 0

    var recordType: RecordType

    var recordCategory: RecordCategory?

    var valid: Boolean = false

    var validations: List<Validation> = emptyList()

    constructor(id: Long, value: String, name: String, order: Long, recordType: RecordType, recordCategory: RecordCategory?, valid: Boolean, validations: List<Validation>) {
        this.id = id
        this.value = value
        this.name = name
        this.order = order
        this.recordType = recordType
        this.recordCategory = recordCategory
        this.valid = valid
        this.validations = validations
    }
}
