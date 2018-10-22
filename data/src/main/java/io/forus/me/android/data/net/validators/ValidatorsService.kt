package io.forus.me.android.data.net.validators

import io.forus.me.android.data.entity.validators.request.CreateValidationRequest
import io.forus.me.android.data.entity.validators.response.ListAllValidationRequests
import io.forus.me.android.data.entity.validators.response.ListAllValidators
import io.forus.me.android.data.entity.validators.response.ValidationRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ValidatorsService {

    @GET("api/v1/platform/validators")
    fun listAllValidators(): Observable<ListAllValidators>

    @GET("api/v1/platform/validator-requests")
    fun listAllValidationRequests(): Observable<ListAllValidationRequests>

    @POST("api/v1/platform/validator-requests")
    fun createValidationRequest(@Body createValidationRequest: CreateValidationRequest): Observable<ValidationRequest>

}