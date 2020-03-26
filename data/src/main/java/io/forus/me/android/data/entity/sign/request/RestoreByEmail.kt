package io.forus.me.android.data.entity.sign.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by pavelpantukhov on 22.12.16.
 */


class RestoreByEmail : Serializable{

    @SerializedName("email")
    @Expose
    var email: String


    constructor(email: String) {
        this.email = email
    }
}