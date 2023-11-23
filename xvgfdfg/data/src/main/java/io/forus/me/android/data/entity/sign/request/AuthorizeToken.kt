package io.forus.me.android.data.entity.sign.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthorizeToken(@SerializedName("auth_token")
                    @Expose
                    var authToken: String)