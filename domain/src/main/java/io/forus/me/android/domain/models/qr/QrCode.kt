package io.forus.me.android.domain.models.qr

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class QrCode {

    enum class Type(val type: String) {
        @SerializedName("auth_token") AUTH_TOKEN("auth_token"),
        @SerializedName("voucher") VOUCHER("voucher"),
        @SerializedName("record") P2P_RECORD("record"),
        @SerializedName("identity") P2P_IDENTITY("identity")
    }

    @SerializedName("type")
    var type: Type

    @SerializedName("value")
    var value: String

    constructor(type: Type, value: String) {
        this.type = type
        this.value = value
    }

    fun toJson(): String{
        return GsonBuilder().create().toJson(this)
    }
}