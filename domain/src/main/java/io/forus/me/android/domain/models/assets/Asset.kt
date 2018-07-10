package io.forus.me.android.domain.models.assets

class Asset {

    constructor(name: String, description: String, type: Type) {
        this.name = name
        this.description = description
        this.type = type
    }

    var name: String = ""
    var description: String = ""
    lateinit var type: Type

}
