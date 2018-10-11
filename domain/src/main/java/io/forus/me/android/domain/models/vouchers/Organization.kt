package io.forus.me.android.domain.models.vouchers

class Organization {

    var id: Long

    var name: String

    var logo: String

    constructor(id: Long, name: String, logo: String) {
        this.id = id
        this.name = name
        this.logo = logo
    }
}