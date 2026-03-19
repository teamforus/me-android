package com.gigawatt.android.data.net.sign.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class EmailValidateRequest : Serializable{

    @SerializedName("email")
    @Expose
    var email: String? = null



}