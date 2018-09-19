package io.forus.me.android.domain.models.records

class RecordCategory{



    var id: Long

    var name: String

    var logo: String = ""
    get() {
        return "https://test.platform.forus.io/assets/category-icons/${name.toLowerCase()}.png"
    }

    var order: Long

    var size: Long

    constructor(id: Long, name: String, order: Long, size: Long = 0) {
        this.id = id
        this.name = name
        this.order = order
        this.size = size
    }
}
