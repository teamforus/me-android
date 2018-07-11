package com.gigawatt.android.data.net.sign

import com.gigawatt.android.data.net.sign.models.request.SignUp
import com.gigawatt.android.data.net.sign.models.request.SignUpByEmail
import io.forus.me.android.data.entity.records.response.RecordCategory
import io.forus.me.android.data.entity.sign.response.AccessToken
import io.forus.me.android.data.entity.sign.response.IdentityPinResult
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult
import io.forus.me.android.data.entity.sign.response.SignUpResult
import io.forus.me.android.data.net.Constants
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by pavel on 30.10.17.
 */
interface RecordsService {

    object Service {
        @JvmStatic  val SERVICE_ENDPOINT : String = Constants.BASE_SERVICE_ENDPOINT
    }


    @GET("api/v1/identity/record-categories")
    fun getCategories() : Observable<List<RecordCategory>>



}