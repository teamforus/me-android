package io.forus.me.android.domain.models.account

class NewAccountRequest {



    constructor(firstname: String? = null, lastname: String? = null, bsn: String? = null, email: String, phoneNumber: String? = null) {
        this.firstname = firstname
        this.lastname = lastname
        this.bsn = bsn
        this.email = email
        this.phoneNumber = phoneNumber
    }

    constructor()

    var firstname: String? = null

    var lastname: String? = null

    var bsn: String? = null

    var email: String = ""

    var phoneNumber: String? = null
}
