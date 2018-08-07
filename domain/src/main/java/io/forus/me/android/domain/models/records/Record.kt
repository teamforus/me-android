package io.forus.me.android.domain.models.records

class Record{

    var id: Long = 0

    var value: String = ""

    var order: Long = 0

    var key: String = ""

    var recordCategoryId: Long = 0

    var valid: Boolean = false

    var validations: List<String> = emptyList()

    constructor(id: Long, value: String, order: Long, key: String, recordCategoryId: Long, valid: Boolean, validations: List<String>) {
        this.id = id
        this.value = value
        this.order = order
        this.key = key
        this.recordCategoryId = recordCategoryId
        this.valid = valid
        this.validations = validations
    }
}
