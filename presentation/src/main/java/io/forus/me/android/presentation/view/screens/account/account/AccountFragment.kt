package io.forus.me.android.presentation.view.screens.account.account

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.domain.models.qr.QrCode
import io.forus.me.android.presentation.BuildConfig
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.helpers.SystemServices
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.models.ChangePinMode
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.about.AboutMeFragment
import io.forus.me.android.presentation.view.screens.account.account.dialogs.LogoutDialog
import io.forus.me.android.presentation.view.screens.dashboard.DashboardActivity
import io.forus.me.android.presentation.view.screens.qr.dialogs.RestoreIdentityDialog
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_account_details.*


/**
 * Fragment User Account Screen.
 */
class AccountFragment : ToolbarLRFragment<AccountModel, AccountView, AccountPresenter>(), AccountView {

    companion object {
        private const val REQUEST_CHANGE_PIN = 10001
    }

    override val allowBack: Boolean
        get() = true

    override val showAccount: Boolean
        get() = false

    override fun viewForSnackbar(): View = lr_panel

    override val toolbarTitle: String
        get() = getString(R.string.dashboard_profile)

    override fun loadRefreshPanel() = lr_panel

    val h = Handler()
    var optionPincodeIsEnable = true
    var preSavedOptionSendCrashLogIsEnable = false


    private var isFingerprintHardwareAvailable = false

    private lateinit var services: SystemServices
    private val sendSupportEmailDialogBuilder: AlertDialog.Builder by lazy(LazyThreadSafetyMode.NONE) {
        AlertDialog.Builder(context!!)
                .setTitle(R.string.send_feedback_email_dialog_title)
                .setNegativeButton(R.string.send_voucher_email_dialog_cancel_button) { dialogInterface, _ -> dialogInterface.dismiss() }
                .setPositiveButton(R.string.send_voucher_email_dialog_positive_button) { dialogInterface: DialogInterface, _ ->
                    dialogInterface.dismiss()
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:${support_email.text}")

                    startActivity(Intent.createChooser(intent, getString(R.string.send_email_title)))
                }

    }

    private val logout = PublishSubject.create<Boolean>()
    override fun logout(): Observable<Boolean> = logout

    private val switchStartFromScanner = PublishSubject.create<Boolean>()
    override fun switchStartFromScanner(): Observable<Boolean> = switchStartFromScanner

    private val switchFingerprint = PublishSubject.create<Boolean>()
    override fun switchFingerprint(): Observable<Boolean> = switchFingerprint

    private val switchSendCrashReports = PublishSubject.create<Boolean>()
    override fun switchSendCrashReports(): Observable<Boolean> = switchSendCrashReports

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_account_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        services = (activity as BaseActivity).systemServices
        isFingerprintHardwareAvailable = services.isFingerprintHardwareAvailable()

        change_digits.setOnClickListener {
            navigator.navigateToChangePin(this, ChangePinMode.CHANGE_OLD, REQUEST_CHANGE_PIN)
        }

        logout_view.setOnClickListener {
            showConfirmLogoutDialog()

        }

        about_me.setOnClickListener {
            (activity as? BaseActivity)?.replaceFragment(AboutMeFragment())
        }

        support_email.setOnClickListener {
            sendSupportEmailDialogBuilder.show()
        }

        headView.setOnClickListener {
            navigator.navigateToRecordsList(context!!, null)
        }

        optionPincodeIsEnable = true

        SharedPref.init(context!!)
        preSavedOptionSendCrashLogIsEnable = SharedPref.read(SharedPref.OPTION_SEND_CRASH_REPORT, false)

        if(preSavedOptionSendCrashLogIsEnable){
            h.postDelayed(object : Runnable{
                override fun run() {
                    optionPincodeIsEnable = true
                    enable_send_crash_log.setChecked(true)
                    preSavedOptionSendCrashLogIsEnable = false
                    SharedPref.write(SharedPref.OPTION_SEND_CRASH_REPORT,false)
                    switchSendCrashReports.onNext(true)
                }
            },600)



        }
    }

    override fun createPresenter():AccountPresenter {

        SharedPref.init(context!!)
        preSavedOptionSendCrashLogIsEnable = SharedPref.read(SharedPref.OPTION_SEND_CRASH_REPORT, false)
        val sendReport =  preSavedOptionSendCrashLogIsEnable
        return AccountPresenter(
                Injection.instance.accountRepository, sendReport
        )
    }


    private fun showConfirmLogoutDialog() {
        LogoutDialog(context!!) { logout.onNext(true) }.show();
    }



    override fun render(vs: LRViewState<AccountModel>) {
        super.render(vs)

        app_version.text = BuildConfig.VERSION_NAME + "-" + BuildConfig.BUILD_NUMBER

        name.text = vs.model.account?.name
        email.text = vs.model.account?.email
        avatar.setImageDrawable(resources.getDrawable(R.drawable.ic_me_logo))

        change_digits.visibility = if (vs.model.pinlockEnabled) View.VISIBLE else View.GONE
        enable_pinlock.setChecked(vs.model.pinlockEnabled)
        enable_pinlock.setOnClickListener {
            if (optionPincodeIsEnable) {


                optionPincodeIsEnable = false
                h.postDelayed(object : Runnable {
                    override fun run() {
                        optionPincodeIsEnable = true
                    }
                }, 1000)
                navigator.navigateToChangePin(this, if (vs.model.pinlockEnabled) ChangePinMode.REMOVE_OLD else ChangePinMode.SET_NEW, REQUEST_CHANGE_PIN)
            }
        }

        enable_fingerprint.visibility = if (isFingerprintHardwareAvailable && vs.model.pinlockEnabled) View.VISIBLE else View.GONE
        enable_fingerprint.setChecked(vs.model.fingerprintEnabled)
        enable_fingerprint.setOnClickListener {

            if (services.hasEnrolledFingerprints()) {
                switchFingerprint.onNext(!vs.model.fingerprintEnabled)
            } else showToastMessage(resources.getString(R.string.lock_no_fingerprints))
        }

        start_from_scanner.setChecked(vs.model.startFromScanner)
        start_from_scanner.setOnClickListener {
            switchStartFromScanner.onNext(!vs.model.startFromScanner)
        }

        enable_send_crash_log.setChecked(vs.model.sendCrashReportsEnabled)
        enable_send_crash_log.setOnClickListener {
            switchSendCrashReports.onNext(!vs.model.sendCrashReportsEnabled)
        }

        if (vs.model.account?.address != null) {
            btn_qr.setOnClickListener {
                (activity as? DashboardActivity)?.showPopupQRFragment(QrCode(QrCode.Type.P2P_IDENTITY, vs.model.account.address).toJson())
            }
        }

        if (vs.model.navigateToWelcome) {
            //navigator.navigateToWelcomeScreen(activity)
            navigator.navigateToLoginSignUp(activity)
            activity?.finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHANGE_PIN && resultCode == Activity.RESULT_OK) {
            updateModel()
        }
    }
}

