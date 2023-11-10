package io.forus.me.android.presentation.api_config.check_api_status

import android.content.Context
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.data.net.common.CommonService
import io.forus.me.android.data.repository.common.CommonRepository
import io.forus.me.android.data.repository.common.datasource.CommonRemoteDataSource
import io.forus.me.android.presentation.api_config.ApiConfig
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CheckApiPresenter(val context: Context) {


    fun checkApi(apiString: String, success: (Boolean) -> Unit, error: (Throwable) -> Unit) {

        try {

            val commonRemoteDataSource = CommonRemoteDataSource { MeServiceFactory.getInstance().createRetrofitService(CommonService::class.java, apiString) }

            val commonRepository = CommonRepository(commonRemoteDataSource)

            commonRepository.status()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map<Boolean> {
                        success(it)
                        it
                    }
                    .onErrorReturn {
                        error(it)
                        false
                    }.subscribe()

        } catch (e: Exception) {
            error(e)
        }
    }
}