package com.gigawatt.android.data.net.sign.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.forus.me.android.data.entity.sign.request.SignRecords
import java.io.Serializable

/**
 * Created by pavelpantukhov on 22.12.16.
 */


class SignUpByEmail : Serializable{

    @SerializedName("email")
    @Expose
    var email: String = ""

    @SerializedName("source")
    @Expose
    var source: String = "app.me_app"

    constructor(email: String) {
        this.email = email
    }
}