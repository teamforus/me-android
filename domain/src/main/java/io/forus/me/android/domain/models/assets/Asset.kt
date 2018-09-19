package io.forus.me.android.domain.models.assets

import kotlin.math.log

class Asset {

    constructor(name: String, description: String, logoUrl: String?, type: Type) {
        this.name = name
        this.description = description
        this.type = type
        this.logoUrl = logoUrl
    }

    var name: String = ""
    var logoUrl: String? = ""
    var description: String = ""
    var type: Type

}
