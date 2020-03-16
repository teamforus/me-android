package io.forus.me.android.data.net.sign

import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.account.Account
import io.forus.me.android.data.entity.common.Success
import io.forus.me.android.data.entity.sign.request.AuthorizeCode
import io.forus.me.android.data.entity.sign.request.AuthorizeToken
import io.forus.me.android.data.entity.sign.request.RegisterPush
import io.forus.me.android.data.entity.sign.request.RestoreByEmail
import io.forus.me.android.data.entity.sign.response.*
import io.reactivex.Observable
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by pavel on 30.10.17.
 */
interface SignService {

    @POST("api/v1/identity")
    fun signup(@Body signUp: SignUp) : Observable<SignUpResult>

    @GET("api/v1/identity")
    fun getIdentity(): Observable<Account>

    @POST("api/v1/identity/proxy/token")
    fun restoreByQrToken() : Observable<IdentityTokenResult>


    @POST("api/v1/identity/proxy/code")
    fun restoreByPinCode() : Observable<IdentityPinResult>


    @POST("api/v1/identity/proxy/email")
    fun restoreByEmail(@Body restore: RestoreByEmail) : Observable<ResponseBody>


    @GET("api/v1/identity/proxy/authorize/email/app-me_app/{token}")
    fun restoreExchangeToken(@Path("token") token: String) : Observable<AccessToken>


    @POST("api/v1/identity/proxy/authorize/code")
    fun authorizeCode(@Body authorizeCode: AuthorizeCode): Observable<Success>


    @POST("api/v1/identity/proxy/authorize/token")
    fun authorizeToken(@Body authorizeToken: AuthorizeToken): Observable<Success>


    @GET("api/v1/identity/proxy/check-token")
    fun checkToken(@Query("access_token") token: String): Observable<CheckTokenResult>


    @POST("api/v1/platform/devices/register-push")
    fun registerPush(@Body token: RegisterPush): Observable<Void>

}