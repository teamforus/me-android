package io.forus.me.android.domain.models.vouchers

class ProductCategory {

    var id: Long

    var key: String?

    var name: String?

    constructor(id: Long, key: String?, name: String?) {
        this.id = id
        this.key = key
        this.name = name
    }
}