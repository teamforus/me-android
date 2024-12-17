package io.forus.me.android.presentation.view.screens.account.account

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import io.forus.me.android.domain.models.qr.QrCode
import io.forus.me.android.presentation.BuildConfig
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentAccountDetailsBinding
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
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/**
 * Fragment User Account Screen.
 */
class AccountFragment : ToolbarLRFragment<AccountModel, AccountView, AccountPresenter>(), AccountView {

    companion object {
        private const val REQUEST_CHANGE_PIN = 10001
    }
    
    private lateinit var binding: FragmentAccountDetailsBinding

    override val allowBack: Boolean
        get() = false

    override val showAccount: Boolean
        get() = false

    override fun viewForSnackbar(): View = binding.lrPanel

    override val toolbarTitle: String
        get() = getString(R.string.dashboard_profile)

    override fun loadRefreshPanel() = binding.lrPanel

    val h = Handler()
    var optionPincodeIsEnable = true
    var preSavedOptionSendCrashLogIsEnable = false


    private var isFingerprintHardwareAvailable = false

    private lateinit var services: SystemServices
    private val sendSupportEmailDialogBuilder: AlertDialog.Builder by lazy(LazyThreadSafetyMode.NONE) {
        AlertDialog.Builder(requireContext())
                .setTitle(R.string.send_feedback_email_dialog_title)
                .setNegativeButton(R.string.send_voucher_email_dialog_cancel_button) { dialogInterface, _ -> dialogInterface.dismiss() }
                .setPositiveButton(R.string.send_voucher_email_dialog_positive_button) { dialogInterface: DialogInterface, _ ->
                    dialogInterface.dismiss()
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:${binding.supportEmail.text}")

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

    private val refreshTrigger = PublishSubject.create<Unit>()
    override fun refreshDataIntent(): Observable<Unit> = refreshTrigger

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        services = (activity as DashboardActivity).systemServices
        isFingerprintHardwareAvailable = services.isFingerprintHardwareAvailable()

        binding.changeDigits.setOnClickListener {
            navigator.navigateToChangePin(this, ChangePinMode.CHANGE_OLD, REQUEST_CHANGE_PIN)
        }

        binding.logoutView.setOnClickListener {
            showConfirmLogoutDialog()

        }

        binding.aboutMe.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_aboutMeFragment)
        }

        binding.privacyPolicyCard.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, 
                Uri.parse(requireContext().getString(R.string.profile_privacy_policy_url)))
            requireContext().startActivity(intent)
        }

        binding.supportEmail.setOnClickListener {
            sendSupportEmailDialogBuilder.show()
        }

        binding.headView.setOnClickListener {
            navigator.navigateToRecordsList(requireContext(), null)
        }

        binding.btAppExplanation.setOnClickListener {
            navigator.navigateToWelcomeScreen(requireContext(),false)
        }

        optionPincodeIsEnable = true

        SharedPref.init(requireContext())
        preSavedOptionSendCrashLogIsEnable = SharedPref.read(SharedPref.OPTION_SEND_CRASH_REPORT, false)

        if(preSavedOptionSendCrashLogIsEnable){
            h.postDelayed(object : Runnable{
                override fun run() {
                    optionPincodeIsEnable = true
                    binding.enableSendCrashLog.setChecked(true)
                    preSavedOptionSendCrashLogIsEnable = false
                    SharedPref.write(SharedPref.OPTION_SEND_CRASH_REPORT,false)
                    switchSendCrashReports.onNext(true)
                }
            },600)



        }
    }

    override fun createPresenter():AccountPresenter {

        SharedPref.init(requireContext())
        preSavedOptionSendCrashLogIsEnable = SharedPref.read(SharedPref.OPTION_SEND_CRASH_REPORT, false)
        val sendReport =  preSavedOptionSendCrashLogIsEnable
        return AccountPresenter(
                Injection.instance.accountRepository, sendReport
        )
    }


    private fun showConfirmLogoutDialog() {
        LogoutDialog(requireContext()) { logout.onNext(true) }.show();
    }



    override fun render(vs: LRViewState<AccountModel>) {
        super.render(vs)

        binding.appVersion.text = BuildConfig.VERSION_NAME + "-" + BuildConfig.BUILD_NUMBER

        binding.name.text = vs.model.account?.name
        binding.email.text = vs.model.account?.email
        binding.avatar.setImageDrawable(resources.getDrawable(R.drawable.ic_me_logo))

        binding.changeDigits.visibility = if (vs.model.pinlockEnabled) View.VISIBLE else View.GONE


        binding.enablePinlock.setChecked(vs.model.pinlockEnabled)



        binding.enablePinlock.setOnClickListener {
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

        binding.enableFingerprint.visibility = if (isFingerprintHardwareAvailable && vs.model.pinlockEnabled) View.VISIBLE else View.GONE
        binding.enableFingerprint.setChecked(vs.model.fingerprintEnabled)
        binding.enableFingerprint.setOnClickListener {

            if (services.hasEnrolledFingerprints()) {
                switchFingerprint.onNext(!vs.model.fingerprintEnabled)
            } else showToastMessage(resources.getString(R.string.lock_no_fingerprints))
        }

        binding.startFromScanner.setChecked(vs.model.startFromScanner)
        binding.startFromScanner.setOnClickListener {
            switchStartFromScanner.onNext(!vs.model.startFromScanner)
        }

        binding.enableSendCrashLog.setChecked(vs.model.sendCrashReportsEnabled)
        binding.enableSendCrashLog.setOnClickListener {
            switchSendCrashReports.onNext(!vs.model.sendCrashReportsEnabled)
        }

        if (vs.model.account?.address != null) {
            binding.btnQr.setOnClickListener {
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

            Handler(Looper.getMainLooper()).postDelayed({
                refreshTrigger.onNext(Unit)
            }, 500)
        }
    }




}

