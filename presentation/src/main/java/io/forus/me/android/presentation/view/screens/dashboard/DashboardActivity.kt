package io.forus.me.android.presentation.view.screens.dashboard

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.crashlytics.android.Crashlytics
import com.google.android.play.core.review.ReviewManagerFactory
import io.fabric.sdk.android.Fabric
import io.forus.me.android.data.executor.JobExecutor
import io.forus.me.android.domain.interactor.CheckLoginUseCase
import io.forus.me.android.domain.interactor.CheckSendCrashReportsEnabled
import io.forus.me.android.domain.interactor.ExitIdentityUseCase
import io.forus.me.android.domain.interactor.LoadAccountUseCase
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.UIThread
import io.forus.me.android.presentation.helpers.InAppReviewHelper
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.activity.SlidingPanelActivity
import io.forus.me.android.presentation.view.fragment.QrFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DashboardActivity : SlidingPanelActivity(), DashboardContract.View {

    private var currentFragment: android.support.v4.app.Fragment? = null
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
                ExitIdentityUseCase(Injection.instance.accountRepository, JobExecutor(), UIThread()))
        presenter.onCreate()

        checkFCM()
        checkStartFromScanner()

        inAppLaunchCount()

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return currentFragment?.onOptionsItemSelected(item) ?: false
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        disposableHolder.disposeAll()
    }

    fun showPopupQRFragment(address: String, qrHead: String? = null, qrSubtitle: String? = null, qrDescription: String? = null) {
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


    private fun inAppLaunchCount(){

       // InAppReviewHelper.resetTotalCountAppLaunch()

        InAppReviewHelper.incrementTotalLaunchCount()
        InAppReviewHelper.incrementLastLaunchCount()

        val canReview = InAppReviewHelper.canLaunchInAppReviewDialog()

        Log.d("inAppRev", " canReview = $canReview")

        //if (canReview && getBuildVersion() != InAppReviewHelper.getLastRateAppVersion())
        //{
            showInAppRevDialog()
       // }

    }

    private fun showInAppRevDialog(){
       // val dialog = MaterialDialog.Builder(this).title("rateApp").show()

        val reviewManager = ReviewManagerFactory.create(this)

        val requestReviewFlow = reviewManager.requestReviewFlow()
        requestReviewFlow.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                Log.d("inAppRev", "listener success")
                val reviewInfo = request.result

                val flow = reviewManager.launchReviewFlow(this, reviewInfo)

                flow.addOnCompleteListener {

                    // Handler complete success
                    InAppReviewHelper.incrementReviewsCount()
                    InAppReviewHelper.resetTimesFromLastRate()
                    InAppReviewHelper.writeCurrentAppVersion(getBuildVersion())
                }

            } else {
                Log.d("inAppRev", " canReview error= ${request.exception}")
                // Handle error

            }
        }


    }



    fun getBuildVersion(): String{
        return try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

}
