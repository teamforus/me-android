package com.gigawatt.android.data.net.sign

import io.forus.me.android.data.entity.common.Success
import io.forus.me.android.data.entity.records.request.*
import io.forus.me.android.data.entity.records.response.*
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

    // Record Types

    @GET("api/v1/identity/record-types")
    fun listAllTypes() : Observable<List<RecordType>>


    // Record Categories

    @GET("api/v1/identity/record-categories")
    fun listAllCategories() : Observable<List<RecordCategory>>

    @POST("api/v1/identity/record-categories")
    fun createCategory(@Body createCategory: CreateCategory) : Observable<Success>

    @GET("api/v1/identity/record-categories/{id}")
    fun retrieveCategory(@Path("id") id: Long) : Observable<RecordCategory>

    @POST("api/v1/identity/record-categories/{id}")
    fun updateCategory(@Path("id") id: Long, @Body updateCategory: UpdateCategory) : Observable<Success>

    @DELETE("api/v1/identity/record-categories/{id}")
    fun deleteCategory(@Path("id") id: Long) : Observable<Success>

    @POST("api/v1/identity/record-categories/sort")
    fun sortCategories(@Body sortCategories: SortCategories) : Observable<Success>


    // Records

    @GET("api/v1/identity/records")
    fun listAllRecords(@Query("type") type: String?, @Query("record_category_id") categoryId: Long?) : Observable<List<Record>>

    @POST("api/v1/identity/records")
    fun createRecord(@Query("type") type: String, @Body createRecord: CreateRecord) : Observable<Success>

    @GET("api/v1/identity/records/{id}")
    fun retrieveRecord(@Path("id") id: Long) : Observable<Record>

    @POST("api/v1/identity/records/{id}")
    fun updateRecord(@Path("id") id: Long, @Body updateRecord: UpdateRecord) : Observable<Success>

    @DELETE("api/v1/identity/records/{id}")
    fun deleteRecord(@Path("id") id: Long) : Observable<Success>

    @POST("api/v1/identity/records/sort")
    fun sortRecords(@Body sortRecords: SortRecords) : Observable<Success>


    // Validations

    @POST("api/v1/identity/record-validations")
    fun createValidationToken(@Body createValidationToken: CreateValidationToken) : Observable<ValidationToken>

    @GET("api/v1/identity/record-validations/{uuid}")
    fun readValidation(@Path("uuid") uuid: String) : Observable<Validation>

    @GET("api/v1/identity/record-validations/{uuid}/approve")
    fun approveValidation(@Path("uuid") uuid: String) : Observable<Success>

    @GET("api/v1/identity/record-validations/{uuid}/decline")
    fun declineValidation(@Path("uuid") uuid: String) : Observable<Success>
}