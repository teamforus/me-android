package com.gigawatt.android.data.net.sign

import io.forus.me.android.data.entity.records.request.CreateRecord
import io.forus.me.android.data.entity.records.response.CreateRecordResult
import io.forus.me.android.data.entity.records.response.Record
import io.forus.me.android.data.entity.records.response.RecordCategory
import io.forus.me.android.data.entity.records.response.RecordType
import io.forus.me.android.data.net.Constants
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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


    // Records

    @GET("api/v1/identity/records")
    fun listAllRecords(@Query("type") type: String) : Observable<List<Record>>

    @POST("api/v1/identity/record")
    fun createRecord(@Body createRecord: CreateRecord) : Observable<CreateRecordResult>

}