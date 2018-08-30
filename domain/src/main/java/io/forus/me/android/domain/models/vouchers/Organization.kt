package io.forus.me.android.domain.models.vouchers

class Organization {

    var id: Long

    var name: String

    constructor(id: Long, name: String) {
        this.id = id
        this.name = name
    }
}