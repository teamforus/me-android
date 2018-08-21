package io.forus.me.android.data.entity.records.response

import com.google.gson.annotations.SerializedName

class RecordCategory(
        @field:SerializedName("id") var id: Long,
        @field:SerializedName("name") var name: String,
        @field:SerializedName("order") var order: Long,
        var logo: String?)
