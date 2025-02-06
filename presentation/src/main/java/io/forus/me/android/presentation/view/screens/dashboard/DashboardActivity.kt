package io.forus.me.android.presentation.view.screens.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityDashboardBinding
import io.forus.me.android.presentation.helpers.SystemServices
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.navigation.Navigator
import io.forus.me.android.presentation.view.MeBottomSheetDialogFragment
import io.forus.me.android.presentation.view.fragment.QrFragment
import io.forus.me.android.presentation.view.screens.vouchers.VoucherViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DashboardActivity : AppCompatActivity() {

     val viewModel: VoucherViewModel by viewModels()
    private val db = Injection.instance.databaseHelper

    val systemServices by lazy(LazyThreadSafetyMode.NONE) { SystemServices(this) }
    val navigator by lazy { Navigator() }

    private val loggingViewModelFactory by lazy {
        LoggingViewModelFactory(Injection.instance.firestoreTokenManager)
    }

    private val loggingViewModel by lazy {
        ViewModelProvider(this, loggingViewModelFactory).get(LoggingViewModel::class.java)
    }



    private var settings = Injection.instance.settingsDataSource
    private var fcmHandler = Injection.instance.fcmHandler
    private var disposableHolder = DisposableHolder()

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, DashboardActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db.open("") //Database initialization

        val navigationHost =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navigationHost.navController



        binding.bottomNavigation.apply {
            setupWithNavController(navController)

            setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.dashboard_property -> {
                        navController.navigate(R.id.vouchersFragment)
                        true
                    }
                    R.id.dashboard_profile -> {
                        navController.navigate(R.id.accountFragment)
                        true
                    }
                    R.id.dashboard_qr -> {
                        navigateToQrScanner()
                        false
                    }
                    else -> false
                }
            }
        }

        checkFCM()

        checkStartFromScanner()

        Handler(Looper.getMainLooper()).postDelayed({
            loggingViewModel.authorizeFirestore()
        },100)
    }



    fun navigateToQrScanner() {
        this.navigator.navigateToQrScanner(this)
    }

    private fun checkStartFromScanner() {
        if (settings.isStartFromScannerEnabled()) {
            navigateToQrScanner()
        }
    }

    fun showPopupQRFragment(address: String,qrHead: String? = null, qrSubtitle: String? = null, qrDescription: String? = null) {

        val meBottomSheet = MeBottomSheetDialogFragment.newInstance(
            QrFragment.newIntent(address, qrHead, qrSubtitle, qrDescription),"QR code")
        meBottomSheet.show(supportFragmentManager, meBottomSheet.tag)
    }

    private fun checkFCM() {
        disposableHolder.add(fcmHandler.checkFCMToken(this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }

    fun logout() {
        fcmHandler.clearFCMToken()
        navigator.navigateToLoginSignUp(this)
        finish()
    }

}

