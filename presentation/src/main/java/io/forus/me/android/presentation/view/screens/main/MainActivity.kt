package io.forus.me.android.presentation.view.screens.main


import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.activity.BaseActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewManagerFactory
import io.forus.me.android.presentation.BuildConfig
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.api_config.ApiConfig
import io.forus.me.android.presentation.api_config.ApiType
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.view.screens.onboarding_screens.OnboardActivity


/**
 * Main application screen. This is the app entry point.
 */
class MainActivity : BaseActivity() {

    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

    private val ONBOARD_INTENT_REQUEST_CODE = 2354

    private val db = Injection.instance.databaseHelper
    private val settings = Injection.instance.settingsDataSource

    var PACKAGE_NAME: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(io.forus.me.android.presentation.R.layout.activity_main)

        PACKAGE_NAME = applicationContext.packageName

        SharedPref.init(this@MainActivity)




        if (!BuildConfig.APPLICATION_ID.equals("io.forus.me")) {
            val savedApiOption = SharedPref.read(SharedPref.OPTION_API_TYPE,"") ?: ""
            if (savedApiOption.isNotEmpty()) {
                val apiType = ApiConfig.stringToApiType(savedApiOption)
                if (apiType == ApiType.OTHER) {
                    ApiConfig.changeToCustomApi(SharedPref.read(SharedPref.OPTION_CUSTOM_API_URL, BuildConfig.SERVER_URL))
                } else {
                    ApiConfig.changeApi(apiType)
                }
            }
        }

        val isGooglePlayAvailable = checkPlayServices()

        //Check inApp update
        h.postDelayed({
            this.initInAppUpdate()
        }, 800)

        requestReviewFlow(this)


        navigateToMainContentScreens()
    }






    fun navigateToMainContentScreens(){
        if (db.exists()) {
           SharedPref.write(SharedPref.IS_MUST_SHOW_ONBOARD_SCREENS, false)
            val locked = settings.isPinEnabled()
            if (locked) {
                navigateToPinlock()
            } else {
                navigateToDashboard()
            }
        } else {
            val isMustShowOnboardScreen = SharedPref.read(SharedPref.IS_MUST_SHOW_ONBOARD_SCREENS, true)
            if(isMustShowOnboardScreen){
                navigateToOnboardScreens()
            }else {
                navigateToLogInsignUpScreen()
            }
        }
    }

    fun navigateToOnboardScreens(){
        startActivityForResult(OnboardActivity.getCallingIntent(this),ONBOARD_INTENT_REQUEST_CODE)
    }


    /**
     * Goes to the welcome screen.
     */
    private fun navigateToLogInsignUpScreen() {

        this.navigator.navigateToLoginSignUp(this)
        finish()
    }


    /**
     * Goes to the welcome screen.
     */
    private fun navigateToWelcomeScreen() {

        this.navigator.navigateToWelcomeScreen(this)
        finish()
    }

    /**
     * Goes to the dashboard screen.
     */
    private fun navigateToDashboard() {
        db.open("")
        this.navigator.navigateToDashboard(this)
        finish()
    }

    /**
     * Goes to the lock screen
     */
    private fun navigateToPinlock() {
        // Database will be opened later
        val useFingerprint = settings.isFingerprintEnabled()
        this.navigator.navigateToDashboardPinlocked(this, useFingerprint)
        finish()
    }

    private fun checkPlayServices(): Boolean {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(this)
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show()
            }

            return false
        }

        return true
    }


    var h = Handler()

    val MY_REQUEST_CODE = 2514

    var appUpdateManager: AppUpdateManager? = null

    private fun initInAppUpdate() {

        appUpdateManager = AppUpdateManagerFactory.create(this@MainActivity)

        if (appUpdateManager == null) return


        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {


                SharedPref.write(SharedPref.OPTION_NEED_APP_UPDATE, true)
                try {
                    //Toast.makeText(this@MainActivity, "initInAppUpdate success", Toast.LENGTH_SHORT).show()
                    updateApp(appUpdateInfo)

                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            } else {
                SharedPref.write(SharedPref.OPTION_NEED_APP_UPDATE, false)
            }
        }

        appUpdateInfoTask.addOnFailureListener { err ->
            processInAppUpdateError(err.localizedMessage)
        }


    }


    @Throws(IntentSender.SendIntentException::class)
    private fun updateApp(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager!!.startUpdateFlowForResult(
                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                appUpdateInfo,
                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                AppUpdateType.IMMEDIATE,
                // The current activity making the update request.
                this,
                // Include a request code to later monitor this update request.
                MY_REQUEST_CODE)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                processInAppUpdateError("")
            }
        }else if(requestCode == ONBOARD_INTENT_REQUEST_CODE){
            if (resultCode != Activity.RESULT_OK) {
                Log.d("forus","ONBOARD_INTENT_REQUEST_CODE")
                SharedPref.write(SharedPref.IS_MUST_SHOW_ONBOARD_SCREENS, false)
                Log.d("forus","IS_MUST_SHOW_ONBOARD_SCREENS = ${SharedPref.read(SharedPref.IS_MUST_SHOW_ONBOARD_SCREENS,true)}")
                navigateToMainContentScreens()
            }
        }
    }

    private fun processInAppUpdateError(error: String) {

        //Toast.makeText(this@MainActivity, "initInAppUpdate error $error", Toast.LENGTH_LONG).show() TODO

    }


    /**
     * InApp rate
     */
    private fun requestReviewFlow(activity: Activity) {

         val dialog = MaterialDialog.Builder(activity)
                .title("rateApp").show()

        /*val reviewManager = ReviewManagerFactory.create(activity)

        val requestReviewFlow = reviewManager.requestReviewFlow()

        requestReviewFlow.addOnCompleteListener { request ->

            if (request.isSuccessful) {

                val reviewInfo = request.result

                val flow = reviewManager.launchReviewFlow(activity, reviewInfo)

                flow.addOnCompleteListener {

                    // Обрабатываем завершение сценария оценки

                }

            } else {

                // Обрабатываем тут ошибку

            }
        }*/
    }


}
