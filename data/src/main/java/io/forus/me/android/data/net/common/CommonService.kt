package io.forus.me.android.data.net.common

import io.forus.me.android.data.entity.common.Success
import io.forus.me.android.data.entity.records.request.*
import io.forus.me.android.data.entity.records.response.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by maestrovs on 03.05.2020
 */
interface CommonService {



    @GET("api/v1/status")
    fun status() : Observable<ResponseBody>



}