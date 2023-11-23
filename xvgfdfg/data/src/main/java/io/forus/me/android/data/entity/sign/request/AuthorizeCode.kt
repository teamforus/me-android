package io.forus.me.android.data.entity.sign.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthorizeCode(@SerializedName("auth_code")
                    @Expose
                    var authCode: String)