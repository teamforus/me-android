package com.gigawatt.android.data.net.sign

import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.common.Success
import io.forus.me.android.data.entity.sign.request.AuthorizeCode
import io.forus.me.android.data.entity.sign.request.AuthorizeToken
import io.forus.me.android.data.entity.sign.request.RestoreByEmail
import io.forus.me.android.data.entity.sign.response.*
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
    fun restoreByQrToken() : Observable<IdentityTokenResult>


    @POST("api/v1/identity/proxy/code")
    fun restoreByPinCode() : Observable<IdentityPinResult>


    @POST("api/v1/identity/proxy/email")
    fun restoreByEmail(@Body restore: RestoreByEmail) : Observable<AccessToken>


    @POST("api/v1/identity/proxy/authorize/code")
    fun authorizeCode(@Body authorizeCode: AuthorizeCode): Observable<Success>


    @POST("api/v1/identity/proxy/authorize/token")
    fun authorizeToken(@Body authorizeToken: AuthorizeToken): Observable<Success>


    @GET("api/v1/identity/proxy/check-token")
    fun checkToken(@Query("access_token") token: String): Observable<CheckTokenResult>

}