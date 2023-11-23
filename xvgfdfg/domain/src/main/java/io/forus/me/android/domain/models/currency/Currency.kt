package io.forus.me.android.domain.models.currency

class Currency {


    constructor()

    constructor(name: String, logoUrl: String = "") {
        this.name = name
        this.logoUrl = logoUrl
    }

    var name: String = ""

    var logoUrl: String = ""


}
