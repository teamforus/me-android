package io.forus.me.android.data.net.sign

import com.gigawatt.android.data.net.sign.models.request.EmailValidateRequest
import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.account.Account
import io.forus.me.android.data.entity.common.Success
import io.forus.me.android.data.entity.sign.request.AuthorizeCode
import io.forus.me.android.data.entity.sign.request.AuthorizeToken
import io.forus.me.android.data.entity.sign.request.RegisterPush
import io.forus.me.android.data.entity.sign.request.RestoreByEmail
import io.forus.me.android.data.entity.sign.response.AccessToken
import io.forus.me.android.data.entity.sign.response.CheckTokenResult
import io.forus.me.android.data.entity.sign.response.FirestoreToken
import io.forus.me.android.data.entity.sign.response.IdentityPinResult
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult
import io.forus.me.android.data.entity.sign.response.ShortTokenResult
import io.forus.me.android.data.entity.sign.response.ValidateEmailResult
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by pavel on 30.10.17.
 */
interface SignService {

    @POST("api/v1/identity")
    fun signup(@Body signUp: SignUp) : Observable<ResponseBody>

    @GET("api/v1/identity")
    fun getIdentity(): Observable<Account>

    @POST("api/v1/identity/proxy/token")
    fun restoreByQrToken() : Observable<IdentityTokenResult>


    @POST("api/v1/identity/proxy/code")
    fun restoreByPinCode() : Observable<IdentityPinResult>


    @POST("api/v1/identity/proxy/email")
    fun restoreByEmail(@Body restore: RestoreByEmail) : Observable<ResponseBody>

   @POST("api/v1/identity/validate/email")
   fun validateEmail(@Body email: EmailValidateRequest) : Observable<ValidateEmailResult>

    @GET("api/v1/identity/proxy/email/exchange/{token}")
    fun restoreExchangeToken(@Path("token") token: String) : Observable<AccessToken>


    @GET("api/v1/identity/proxy/confirmation/exchange/{token}")
    fun registerExchangeToken(@Path("token") token: String) : Observable<AccessToken>


    @POST("api/v1/identity/proxy/authorize/code")
    fun authorizeCode(@Body authorizeCode: AuthorizeCode): Observable<Success>


    @POST("api/v1/identity/proxy/authorize/token")
    fun authorizeToken(@Body authorizeToken: AuthorizeToken): Observable<ResponseBody>


    @GET("api/v1/identity/proxy/check-token")
    fun checkToken(@Header("Access-Token") token: String): Observable<CheckTokenResult>


    @POST("api/v1/platform/devices/register-push")
    fun registerPush(@Body token: RegisterPush): Observable<Void>

    @POST("api/v1/identity/proxy/short-token")
    fun getShortToken() : Observable<ShortTokenResult>


    @POST("api/v1/platform/firestore-tokens")
    fun getFirestoreToken(@Body key: Map<String, String>): Observable<FirestoreToken>

}