package io.forus.me.android.presentation.view.screens.account.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.SystemServices
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.models.ChangePinMode
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_account_details.*


/**
 * Fragment User Account Screen.
 */
class AccountFragment : ToolbarLRFragment<AccountModel, AccountView, AccountPresenter>(), AccountView  {

    companion object {
        private const val REQUEST_CHANGE_PIN = 10001
    }

    override val allowBack: Boolean
        get() = true


    override fun viewForSnackbar(): View = lr_panel

    override val toolbarTitle: String
        get() = ""

    override fun loadRefreshPanel() = lr_panel

    private var isFingerprintHardwareAvailable = false

    private lateinit var services: SystemServices

    private val logout = PublishSubject.create<Boolean>()
    override fun logout(): Observable<Boolean> = logout

    private val switchFingerprint = PublishSubject.create<Boolean>()
    override fun switchFingerprint(): Observable<Boolean> = switchFingerprint

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_account_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        services = (activity as BaseActivity).systemServices
        isFingerprintHardwareAvailable = services.isFingerprintHardwareAvailable()

        change_digits.setOnClickListener {
            navigator.navigateToChangePin(this, ChangePinMode.CHANGE_OLD, REQUEST_CHANGE_PIN)
        }

        logout_view.setOnClickListener {
            logout.onNext(true)
        }
    }

    override fun createPresenter() = AccountPresenter(
            Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<AccountModel>) {
        super.render(vs)

        name.text = vs.model.account?.name
        email.text = vs.model.account?.email
        avatar.setImageDrawable(resources.getDrawable(R.drawable.ic_me_logo))

        change_digits.visibility = if(vs.model.pinlockEnabled) View.VISIBLE else View.GONE
        enable_pinlock.setChecked(vs.model.pinlockEnabled)
        enable_pinlock.setOnClickListener{
            navigator.navigateToChangePin(this, if(vs.model.pinlockEnabled) ChangePinMode.REMOVE_OLD else ChangePinMode.SET_NEW, REQUEST_CHANGE_PIN)
        }

        enable_fingerprint.visibility = if(isFingerprintHardwareAvailable && vs.model.pinlockEnabled) View.VISIBLE else View.GONE
        enable_fingerprint.setChecked(vs.model.fingerprintEnabled)
        enable_fingerprint.setOnClickListener {
            if(services.hasEnrolledFingerprints()){
                switchFingerprint.onNext(!vs.model.fingerprintEnabled)
            }
            else showToastMessage(resources.getString(R.string.lock_no_fingerprints))
        }

        if (vs.model.navigateToWelcome) {
            navigator.navigateToWelcomeScreen(activity)
            activity?.finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHANGE_PIN && resultCode == Activity.RESULT_OK){
            updateModel()
        }
    }
}

