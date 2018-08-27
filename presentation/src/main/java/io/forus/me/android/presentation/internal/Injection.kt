package io.forus.me.android.presentation.internal

import android.content.Context
import com.gigawatt.android.data.net.sign.RecordsService
import com.gigawatt.android.data.net.sign.SignService
import io.forus.me.android.data.entity.database.DaoSession
import io.forus.me.android.data.exception.RetrofitExceptionMapper
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.data.repository.account.datasource.local.AccountLocalDataSource
import io.forus.me.android.data.repository.account.datasource.remote.AccountRemoteDataSource
import io.forus.me.android.data.repository.records.RecordsRepository
import io.forus.me.android.data.repository.records.ValidationRepository
import io.forus.me.android.data.repository.records.datasource.mock.RecordsMockDataSource
import io.forus.me.android.data.repository.records.datasource.remote.RecordsRemoteDataSource
import io.forus.me.android.data.repository.web3.datasource.Web3DataSource
import io.forus.me.android.data.repository.web3.datasource.local.Web3LocalDataSource
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.domain.repository.assets.AssetsRepository
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.domain.repository.wallets.WalletsRepository
import io.forus.me.android.presentation.DatabaseHelper
import io.forus.me.android.presentation.view.screens.qr.QrDecoder
import io.forus.me.android.presentation.helpers.reactivex.AccessTokenChecker

class Injection private constructor() {

    init {
        println("This ($this) is a singleton")
    }
    private object Holder {
        val INSTANCE = Injection()
    }
    companion object {
        val instance: Injection by lazy { Holder.INSTANCE }
    }


    var daoSession: DaoSession? = null
        set(value) {
            field = value
            accountLocalDataSource.updateDao(daoSession)
            accountRemoteDataSource.updateService()
            recordRemoteDataSource.updateService()
        }

    var applicationContext: Context? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }

    val databaseHelper: DatabaseHelper by lazy {
        return@lazy DatabaseHelper(applicationContext!!)
    }

    val accountRepository: AccountRepository by lazy {
            return@lazy io.forus.me.android.data.repository.account.AccountRepository(accountLocalDataSource, accountRemoteDataSource)
    }

    private val accountRemoteDataSource: AccountRemoteDataSource by lazy {
        return@lazy AccountRemoteDataSource{ MeServiceFactory.getInstance().createRetrofitService(SignService::class.java, SignService.Service.SERVICE_ENDPOINT) }
    }

    public val accountLocalDataSource: AccountLocalDataSource by lazy {
        return@lazy AccountLocalDataSource(daoSession?.tokenDao)
    }

    private val web3LocalDataSource: Web3DataSource by lazy {
        return@lazy Web3LocalDataSource(applicationContext!!)
    }

    val walletsRepository: WalletsRepository by lazy {
        return@lazy io.forus.me.android.data.repository.wallets.WalletsRepository()
    }

    val assetsRepository: AssetsRepository by lazy {
        return@lazy io.forus.me.android.data.repository.assets.AssetsRepository()
    }

    val vouchersRepository: VouchersRepository by lazy {
        return@lazy io.forus.me.android.data.repository.vouchers.VouchersRepository()
    }


    val recordsRepository: RecordsRepository by lazy {
        return@lazy io.forus.me.android.data.repository.records.RecordsRepository(recordRemoteDataSource)
    }

    private val recordsMockDataSource: RecordsMockDataSource by lazy{
        return@lazy RecordsMockDataSource()
    }

    private val recordRemoteDataSource: RecordsRemoteDataSource by lazy {
        return@lazy RecordsRemoteDataSource{MeServiceFactory.getInstance().createRetrofitService(RecordsService::class.java, RecordsService.Service.SERVICE_ENDPOINT)}
    }

    val retrofitExceptionMapper: RetrofitExceptionMapper by lazy {
        return@lazy RetrofitExceptionMapper()
    }

    val validationRepository: ValidationRepository by lazy {
        return@lazy ValidationRepository()
    }

    val accessTokenChecker: AccessTokenChecker by lazy {
        return@lazy AccessTokenChecker()
    }

    val qrDecoder: QrDecoder by lazy {
        return@lazy QrDecoder(accountRepository, recordsRepository)
    }

}
