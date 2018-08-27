package io.forus.me.android.data.net.validators

import io.forus.me.android.data.entity.validators.response.ListAllValidators
import io.forus.me.android.data.net.Constants
import io.reactivex.Observable
import retrofit2.http.GET

interface ValidatorsService {

    object Service {
        @JvmStatic
        val SERVICE_ENDPOINT: String = Constants.BASE_SERVICE_ENDPOINT
    }

    @GET("api/v1/platform/validators")
    fun listAllValidators(): Observable<ListAllValidators>

}