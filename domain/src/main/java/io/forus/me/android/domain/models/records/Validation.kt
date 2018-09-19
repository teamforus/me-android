package io.forus.me.android.domain.models.records

import com.google.gson.annotations.SerializedName
import java.util.*

class Validation {

    enum class State {
        pending, approved, declined
    }

    @SerializedName("state")
    var state: State

    @SerializedName("identity_address")
    var identityAddress: String? = null

    @SerializedName("created_at")
    var createdAt: Date? = null

    @SerializedName("updated_at")
    var updatedAt: Date? = null

    @SerializedName("uuid")
    var uuid: String? = null

    @SerializedName("value")
    var value: String? = null

    @SerializedName("key")
    var key: String? = null

    @SerializedName("name")
    var name: String? = null

    constructor(state: State, identityAddress: String?, createdAt: Date?, updatedAt: Date?, uuid: String?, value: String?, key: String?, name: String?) {
        this.state = state
        this.identityAddress = identityAddress
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.uuid = uuid
        this.value = value
        this.key = key
        this.name = name
    }

    companion object {
        var p2pIcon = "https://cdn1.iconfinder.com/data/icons/business-models/512/p2p-512.png"
    }
}