package io.forus.me.android.presentation.api_config.check_api_status

import android.content.Context
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.data.net.common.CommonService
import io.forus.me.android.data.repository.common.CommonRepository
import io.forus.me.android.data.repository.common.datasource.CommonRemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CheckApiPresenter(val context: Context) {
    fun checkApi(apiString: String, success: (Boolean) -> Unit, error: (Throwable) -> Unit) {
        try {
            val commonRemoteDataSource = CommonRemoteDataSource { MeServiceFactory.getInstance().createRetrofitService(CommonService::class.java, apiString) }
            val commonRepository = CommonRepository(commonRemoteDataSource)

            commonRepository.status()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { result ->
                    success(result)
                    result
                }
                .onErrorReturn { throwable ->
                    error(throwable)
                    false
                }.subscribe()
        } catch (e: Exception) {
            error(e)
        }
    }
}