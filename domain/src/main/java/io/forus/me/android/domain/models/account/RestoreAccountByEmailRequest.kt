package io.forus.me.android.domain.models.account

class RestoreAccountByEmailRequest {


    constructor(email: String = "") {

        this.email = email
    }


    var email: String = ""

}

