package io.forus.me.android.data.net.vouchers

import io.forus.me.android.data.entity.vouchers.request.MakeTransaction
import io.forus.me.android.data.entity.vouchers.response.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VouchersService {


    /*
 budget:
{{apiUrl}}/platform/provider/vouchers/0xc87fbc0f088057b25b3831c7fc905bc36744f462
product_vouchers:
{{apiUrl}}/platform/provider/vouchers/0xc87fbc0f088057b25b3831c7fc905bc36744f462/product-vouchers

  */


    @GET("api/v1/platform/vouchers")
    fun listAllVouchers(): Observable<ListAllVouchers>

    @GET("api/v1/platform/vouchers/{address}")
    fun getVoucher(@Path("address") address: String): Observable<GetVoucher>

    @GET("api/v1/platform/provider/vouchers/{address}/product-vouchers")
    fun getProductVouchersAsProvider(@Path("address") address: String): Observable<ListAllVouchers>

    @GET("api/v1/platform/vouchers/{address}/transactions")
    fun getTransactions(@Path("address") address: String): Observable<List<Transaction>>

    @GET("api/v1/platform/vouchers/{address}/transactions/{transaction_address}")
    fun getTransaction(@Path("address") address: String, @Path("transaction_address") transactionAddress: String): Observable<Transaction>

    @GET("api/v1/platform/provider/vouchers/{address}")
    fun getVoucherAsProvider(@Path("address") address: String): Observable<GetVoucher>

    @POST("api/v1/platform/vouchers/{address}/transactions")
    fun makeTransaction(@Path("address") address: String, @Body makeTransaction: MakeTransaction): Observable<CreatedTransaction>

    @POST("api/v1/platform/vouchers/{address}/send-email")
    fun sendEmail(@Path("address") address: String): Observable<Void>

}