package io.forus.me.android.domain.models.account

class NewAccountRequest {

    constructor(email: String) {
        this.email = email
    }

    constructor()
    var email: String = ""
}