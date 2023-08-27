package io.forus.me.android.presentation.view.screens.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.forus.me.android.data.executor.JobExecutor
import io.forus.me.android.domain.interactor.CheckLoginUseCase
import io.forus.me.android.domain.interactor.CheckSendCrashReportsEnabled
import io.forus.me.android.domain.interactor.ExitIdentityUseCase
import io.forus.me.android.domain.interactor.LoadAccountUseCase
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.UIThread
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.activity.SlidingPanelActivity
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.fragment.QrFragment
import io.forus.me.android.presentation.view.screens.vouchers.VoucherViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.google.firebase.auth.FirebaseAuth
import io.forus.me.android.presentation.view.screens.account.newaccount.pin.NewPinViewModel


class DashboardActivity : SlidingPanelActivity(), DashboardContract.View,
    MViewModelProvider<VoucherViewModel> {


    override val viewModel: VoucherViewModel by viewModels()

    private val myViewModelFactory by lazy {
        LoggingViewModelFactory(Injection.instance.accountRepository)
    }

    private val loggingViewModel by lazy {
        ViewModelProvider(this, myViewModelFactory).get(LoggingViewModel::class.java)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


        loggingViewModel.firestoreToken.observe(this, Observer {
            registerFirestoreUser(it)
        })

        Handler(Looper.getMainLooper()).postDelayed({
            loggingViewModel.getAccountData()
        },100)

    }


    fun registerFirestoreUser(customToken: String){
        FirebaseAuth.getInstance().signInWithCustomToken(customToken)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = FirebaseAuth.getInstance().currentUser
                    Log.d("LoggingViewModel", "signInWithCustomToken:success $user")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoggingViewModel", "signInWithCustomToken:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.",
                       Toast.LENGTH_SHORT).show()
                    //updateUserUI(null)
                }

            }
    }

    private fun checkFCM() {
        disposableHolder.add(fcmHandler.checkFCMToken(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }

    override fun addUserId(id: String) {
        if (Fabric.isInitialized()) {
            Crashlytics.setUserIdentifier(id)
        }
    }

    override fun logout() {
        fcmHandler.clearFCMToken()
        //navigator.navigateToWelcomeScreen(this)
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
        addPopupFragment(QrFragment.newIntent(address, qrHead, qrSubtitle, qrDescription), "QR code")
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
