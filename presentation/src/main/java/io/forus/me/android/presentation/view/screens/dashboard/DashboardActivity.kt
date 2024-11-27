package io.forus.me.android.presentation.view.screens.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.forus.me.android.data.executor.JobExecutor
import io.forus.me.android.domain.interactor.CheckLoginUseCase
import io.forus.me.android.domain.interactor.CheckSendCrashReportsEnabled
import io.forus.me.android.domain.interactor.ExitIdentityUseCase
import io.forus.me.android.domain.interactor.LoadAccountUseCase
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.UIThread
import io.forus.me.android.presentation.databinding.ActivityDashboardBinding
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.MeBottomSheetDialogFragment
import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.fragment.QrFragment
import io.forus.me.android.presentation.view.screens.vouchers.VoucherViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DashboardActivity() : CommonActivity(), DashboardContract.View,
    MViewModelProvider<VoucherViewModel> {


    override val viewModel: VoucherViewModel by viewModels()

    private val loggingViewModelFactory by lazy {
        LoggingViewModelFactory(Injection.instance.firestoreTokenManager)
    }

    private val loggingViewModel by lazy {
        ViewModelProvider(this, loggingViewModelFactory).get(LoggingViewModel::class.java)
    }

    private var currentFragment: Fragment? = null
    private var menu: Menu? = null

    private var settings = Injection.instance.settingsDataSource
    private var fcmHandler = Injection.instance.fcmHandler
    private var disposableHolder = DisposableHolder()

    private lateinit var presenter: DashboardContract.Presenter

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, DashboardActivity::class.java)
        }
    }

    override val viewID: Int
        get() = R.layout.activity_dashboard

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(R.id.dashboard_content, DashboardFragment())
        presenter = DashboardPresenter(this,
                CheckLoginUseCase(Injection.instance.accountRepository, JobExecutor(), UIThread()),
                LoadAccountUseCase(JobExecutor(), UIThread(), Injection.instance.accountRepository),
                CheckSendCrashReportsEnabled(Injection.instance.accountRepository, JobExecutor(), UIThread()),
                ExitIdentityUseCase(Injection.instance.accountRepository, JobExecutor(), UIThread()),
            Injection.instance.accountRepository)
        presenter.onCreate()

        checkFCM()
        checkStartFromScanner()


        Handler(Looper.getMainLooper()).postDelayed({
            loggingViewModel.authorizeFirestore()
        },100)



    }



    private fun checkFCM() {
        disposableHolder.add(fcmHandler.checkFCMToken(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }

    override fun addUserId(id: String) {
      //  if (Fabric.isInitialized()) {
         //   Crashlytics.setUserIdentifier(id)
      //  }
    }

    override fun logout() {
        fcmHandler.clearFCMToken()
        navigator.navigateToLoginSignUp(this)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return currentFragment?.onOptionsItemSelected(item) ?: false
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        disposableHolder.disposeAll()
    }

    fun showPopupQRFragment(address: String,qrHead: String? = null, qrSubtitle: String? = null, qrDescription: String? = null) {

        val meBottomSheet = MeBottomSheetDialogFragment.newInstance(
            QrFragment.newIntent(address, qrHead, qrSubtitle, qrDescription),"QR code")
        meBottomSheet.show(supportFragmentManager, meBottomSheet.tag)
    }


    override fun replaceFragment(fragment: Fragment, sharedViews: List<View>) {
        super.replaceFragment(R.id.dashboard_top_content, fragment, sharedViews, true)
    }

    fun navigateToQrScanner() {
        this.navigator.navigateToQrScanner(this)
    }

    private fun checkStartFromScanner() {
        if (settings.isStartFromScannerEnabled()) {
            navigateToQrScanner()
        }
    }



}
