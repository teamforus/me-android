package io.forus.me.android.domain.models.records

class CreateRecordResponse{

    var id: Long = 0

    var value: String = ""

    var order: Long = 0


    constructor(id: Long, value: String, order: Long) {
        this.id = id
        this.value = value
        this.order = order
    }
}