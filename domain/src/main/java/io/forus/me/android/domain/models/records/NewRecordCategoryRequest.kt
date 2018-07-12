package io.forus.me.android.domain.models.records

class NewRecordCategoryRequest {


    var name: String = ""
    var order: Long = 0


    constructor(name: String, order: Long) {
        this.name = name
        this.order = order
    }
}