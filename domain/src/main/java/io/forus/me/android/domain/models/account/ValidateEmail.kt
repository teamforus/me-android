package io.forus.me.android.domain.models.account

class ValidateEmail{

    var used: Boolean

    var valid: Boolean

    constructor(used: Boolean, valid: Boolean) {
        this.used = used
        this.valid = valid
    }


}