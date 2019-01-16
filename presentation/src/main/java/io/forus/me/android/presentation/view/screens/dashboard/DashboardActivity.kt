package io.forus.me.android.presentation.view.screens.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.forus.me.android.domain.models.account.Account
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.activity.SlidingPanelActivity
import io.forus.me.android.presentation.view.fragment.QrFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : SlidingPanelActivity(), DashboardContract.View {

    private var currentFragment: android.support.v4.app.Fragment? = null
    private var menu: Menu? = null

    private var accountRepository = Injection.instance.accountRepository
    private var settings = Injection.instance.settingsDataSource
    private var fcmHandler = Injection.instance.fcmHandler
    private var disposableHolder = DisposableHolder()

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

        checkLogin()
        checkFCM()
        checkStartFromScanner()
    }

    private fun checkLogin() {
        disposableHolder.add(Single.fromObservable(accountRepository.checkCurrentToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    if (!it) logout()
                    else if (Fabric.isInitialized()) {
                        accountRepository.getAccount()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { a: Account? ->
                                    a?.let { account ->

                                        Crashlytics.setUserIdentifier(account.address)
                                    }
                                }
                    }
                }
                .onErrorReturn { }
                .subscribe())
    }

    private fun checkFCM() {
        disposableHolder.add(fcmHandler.checkFCMToken(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }

    override fun addUserId(id: String) {
//        if (Fabric.isInitialized()) {
//            Crashlytics.setUserIdentifier(id)
//        }
    }

    override fun logout(){
        disposableHolder.add(Single.fromObservable(accountRepository.exitIdentity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    try {
                        Single.fromObservable(fcmHandler.clearFCMToken())
                                .map {
                                    navigator.navigateToWelcomeScreen(this)
                                    finish()
                                }
                    } catch (e: Exception) {
                        Log.e("DASH_LOGOUT", e.message, e)
                        Single.just(it)
                    }
                }
                .onErrorReturn { }
                .subscribe())
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return currentFragment?.onOptionsItemSelected(item) ?: false
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableHolder.disposeAll()
    }

    fun showPopupQRFragment(address: String, qrSubtitle: String? = null, qrDescription: String? = null) {
        addPopupFragment(QrFragment.newIntent(address, qrSubtitle, qrDescription), "QR code")
    }

    override fun replaceFragment(fragment: Fragment, sharedViews: List<View>) {
        super.replaceFragment(R.id.dashboard_content, fragment, sharedViews, true)
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
