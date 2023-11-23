package com.gigawatt.android.data.net.sign.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.forus.me.android.data.entity.sign.request.SignRecords
import java.io.Serializable

/**
 * Created by pavelpantukhov on 22.12.16.
 */


class SignUp : Serializable{

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("pin_code")
    @Expose
    var pinCode: String? = null

    @SerializedName("category")
    @Expose
    var type: String = "personal"


    @SerializedName("records")
    @Expose
    var records: SignRecords? = null

}