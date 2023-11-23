package io.forus.me.android.presentation.internal

import android.content.Context
import android.util.Log
//import com.google.firebase.auth.FirebaseAuth
import io.forus.me.android.data.net.records.RecordsService
import io.forus.me.android.data.net.sign.SignService
import io.forus.me.android.data.entity.database.DaoSession
import io.forus.me.android.data.exception.RetrofitExceptionMapper
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.data.net.common.CommonService
import io.forus.me.android.data.net.validators.ValidatorsService
import io.forus.me.android.data.net.vouchers.VouchersService
import io.forus.me.android.data.repository.account.datasource.local.AccountLocalDataSource
import io.forus.me.android.data.repository.account.datasource.remote.AccountRemoteDataSource
import io.forus.me.android.data.repository.account.datasource.remote.CheckActivationDataSource
import io.forus.me.android.data.repository.common.CommonRepository
import io.forus.me.android.data.repository.common.datasource.CommonDataSource
import io.forus.me.android.data.repository.common.datasource.CommonRemoteDataSource
import io.forus.me.android.data.repository.records.RecordsRepository
import io.forus.me.android.data.repository.records.datasource.mock.RecordsMockDataSource
import io.forus.me.android.data.repository.records.datasource.remote.RecordsRemoteDataSource
import io.forus.me.android.data.repository.settings.SettingsDataSource
import io.forus.me.android.data.repository.settings.SettingsLocalDataSource
import io.forus.me.android.data.repository.validators.ValidatorsRepository
import io.forus.me.android.data.repository.validators.datasource.remote.ValidatorsRemoteDataSource
import io.forus.me.android.data.repository.vouchers.datasource.VouchersDataSource
import io.forus.me.android.data.repository.vouchers.datasource.remote.VouchersRemoteDataSource
import io.forus.me.android.data.repository.web3.datasource.Web3DataSource
import io.forus.me.android.data.repository.web3.datasource.local.Web3LocalDataSource
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.domain.repository.assets.AssetsRepository
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.domain.repository.wallets.WalletsRepository
import io.forus.me.android.presentation.api_config.ApiConfig
import io.forus.me.android.presentation.BuildConfig
import io.forus.me.android.presentation.DatabaseHelper
//import io.forus.me.android.presentation.firestore_logging.FirestoreTokenManager
import io.forus.me.android.presentation.helpers.AppSettings
import io.forus.me.android.presentation.helpers.reactivex.AccessTokenChecker
import io.forus.me.android.presentation.qr.QrDecoder
import io.forus.me.android.presentation.helpers.fcm.FCMHandler

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
        }

    fun accessTokenUpdated() {
        accountRemoteDataSource.updateService()
        recordRemoteDataSource.updateService()
    }

    var applicationContext: Context? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }




    val commonRepository: CommonRepository by lazy {
        return@lazy io.forus.me.android.data.repository.common.CommonRepository(commonRemoteDataSource)
    }


    private val commonRemoteDataSource: CommonRemoteDataSource by lazy {
        return@lazy CommonRemoteDataSource{MeServiceFactory.getInstance().createRetrofitService(CommonService::class.java, ApiConfig.SERVER_URL) }
    }


    val databaseHelper: DatabaseHelper by lazy {
        return@lazy DatabaseHelper(applicationContext!!)
    }

    val accountRepository: AccountRepository by lazy {
        return@lazy io.forus.me.android.data.repository.account.AccountRepository(settingsDataSource, accountLocalDataSource, accountRemoteDataSource, checkActivationDataSource, recordsRepository)
    }

   // val firestoreTokenManager: FirestoreTokenManager by lazy {
   //     return@lazy FirestoreTokenManager(accountRepository)
   // }

    val settingsDataSource: SettingsDataSource by lazy {
        return@lazy SettingsLocalDataSource(AppSettings(applicationContext!!))
    }

    private val accountRemoteDataSource: AccountRemoteDataSource by lazy {
        return@lazy AccountRemoteDataSource { MeServiceFactory.getInstance().createRetrofitService(SignService::class.java, ApiConfig.SERVER_URL) }
    }

    val accountLocalDataSource: AccountLocalDataSource by lazy {
        return@lazy AccountLocalDataSource(daoSession?.tokenDao)
    }

    private val checkActivationDataSource: CheckActivationDataSource by lazy {
        return@lazy CheckActivationDataSource(MeServiceFactory.getInstance().createRetrofitService(SignService::class.java, ApiConfig.SERVER_URL))
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

    val vouchersDataSource: VouchersDataSource by lazy {
        return@lazy VouchersRemoteDataSource { MeServiceFactory.getInstance().createRetrofitService(VouchersService::class.java, ApiConfig.SERVER_URL) }
    }

    val vouchersRepository: VouchersRepository by lazy {
        return@lazy io.forus.me.android.data.repository.vouchers.VouchersRepository(vouchersDataSource)
    }


    val recordsRepository: RecordsRepository by lazy {
        return@lazy io.forus.me.android.data.repository.records.RecordsRepository(recordRemoteDataSource)
    }

    private val recordsMockDataSource: RecordsMockDataSource by lazy {
        return@lazy RecordsMockDataSource()
    }

    private val recordRemoteDataSource: RecordsRemoteDataSource by lazy {
        return@lazy RecordsRemoteDataSource { MeServiceFactory.getInstance().createRetrofitService(RecordsService::class.java, ApiConfig.SERVER_URL) }
    }

    val retrofitExceptionMapper: RetrofitExceptionMapper by lazy {
        return@lazy RetrofitExceptionMapper()
    }

    private val validatorsRemoteDataSource: ValidatorsRemoteDataSource by lazy {
        return@lazy ValidatorsRemoteDataSource { MeServiceFactory.getInstance().createRetrofitService(ValidatorsService::class.java, ApiConfig.SERVER_URL) }
    }

    val validatorsRepository: ValidatorsRepository by lazy {
        return@lazy ValidatorsRepository(validatorsRemoteDataSource)
    }

    val accessTokenChecker: AccessTokenChecker by lazy {
        return@lazy AccessTokenChecker(ApiConfig.SERVER_URL)
    }

    val qrDecoder: QrDecoder by lazy {
        return@lazy QrDecoder()
    }

    val fcmHandler: FCMHandler by lazy {
        return@lazy FCMHandler(accountRepository, settingsDataSource)
    }
}
