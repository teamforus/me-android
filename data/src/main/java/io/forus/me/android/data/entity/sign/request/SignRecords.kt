package io.forus.me.android.data.entity.sign.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SignRecords: Serializable {

    constructor(email: String?) {
        this.email = email
    }

    @SerializedName("primary_email")
    @Expose
    var email: String? = null
}
