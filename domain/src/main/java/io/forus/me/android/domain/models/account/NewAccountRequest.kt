package io.forus.me.android.domain.models.account

class NewAccountRequest {



    constructor(firstname: String? = "", lastname: String? = "", bsn: String? = "", email: String? = "", phoneNumber: String? = "") {
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

    var email: String? = null

    var phoneNumber: String? = null
}
