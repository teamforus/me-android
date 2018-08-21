package io.forus.me.android.domain.models.account

class Identity{

    var accessToken: String

    var pin: String

    constructor(accessToken: String, pin: String) {
        this.accessToken = accessToken
        this.pin = pin
    }


}