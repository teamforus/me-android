package io.forus.me.android.domain.models.account

class ChangePin{

    var oldPin: String

    var newPin: String

    constructor(oldPin: String, newPin: String) {
        this.oldPin = oldPin
        this.newPin = newPin
    }
}