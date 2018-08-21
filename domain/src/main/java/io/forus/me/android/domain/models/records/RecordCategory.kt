package io.forus.me.android.domain.models.records

class RecordCategory{



    var id: Long

    var name: String

    var logo: String

    var order: Long

    var size: Long

    constructor(id: Long, name: String, order: Long, logo: String = "https://www.freelogodesign.org/Content/img/logo-ex-7.png", size: Long = 0) {
        this.id = id
        this.name = name
        this.order = order
        this.logo = logo
        this.size = size
    }
}
