package io.forus.me.android.data.entity.records.request

class NewRecordCategoryRequest {

    var order: Long = 0

    var name: String = ""

    constructor(name: String, order: Long) {
        this.order = order
        this.name = name
    }

}