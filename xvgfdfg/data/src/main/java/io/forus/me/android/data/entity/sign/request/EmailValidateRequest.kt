package com.gigawatt.android.data.net.sign.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.forus.me.android.data.entity.sign.request.SignRecords
import java.io.Serializable



class EmailValidateRequest : Serializable{

    @SerializedName("email")
    @Expose
    var email: String? = null



}