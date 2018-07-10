package com.gigawatt.android.data.net.sign

import com.gigawatt.android.data.net.sign.models.request.SignUp
import com.gigawatt.android.data.net.sign.models.request.SignUpByEmail
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
interface SignService {

    object Service {
        @JvmStatic  val SERVICE_ENDPOINT : String = Constants.BASE_SERVICE_ENDPOINT
    }


    @POST("api/v1/identity")
    fun signup(@Body signUp: SignUp) : Observable<SignUpResult>



    @POST("api/v1/identity/proxy/token")
    fun identityToken() : Observable<IdentityTokenResult>


    @POST("api/v1/identity/proxy/code")
    fun authCode() : Observable<IdentityPinResult>


    @POST("api/v1/identity/proxy/email")
    fun requestNewUserByEmail(@Body signUp: SignUpByEmail) : Observable<AccessToken>



}