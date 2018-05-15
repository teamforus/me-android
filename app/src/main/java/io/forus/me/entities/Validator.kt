package io.forus.me.entities

import android.arch.persistence.room.Entity

@Entity
class Validator(
    var address: String = "",
    var name: String = ""
)