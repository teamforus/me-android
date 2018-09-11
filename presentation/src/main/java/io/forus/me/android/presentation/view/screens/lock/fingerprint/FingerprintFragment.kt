package io.forus.me.android.presentation.view.screens.lock.fingerprint

import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.multidots.fingerprintauth.FingerPrintAuthCallback
import com.multidots.fingerprintauth.FingerPrintAuthHelper
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.lock.PinLockActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_fingerprint.*

class FingerprintFragment : ToolbarLRFragment<FingerprintModel, FingerprintView, FingerprintPresenter>(), FingerprintView {

    companion object {
        fun newIntent(): FingerprintFragment = FingerprintFragment()
    }

    override val showAccount: Boolean
        get() = false


    override val allowBack: Boolean
        get() = false


    override val toolbarTitle: String
        get() = resources.getString(R.string.lock_fingerprint_title)

    private lateinit var mFingerPrintAuthHelper: FingerPrintAuthHelper

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun exit(): Observable<Unit> = RxView.clicks(btn_exit).map { Unit }

    private val authSuccess = PublishSubject.create<Unit>()
    override fun authSuccess(): Observable<Unit> = authSuccess

    private val authFail = PublishSubject.create<String>()
    override fun authFail(): Observable<String> = authFail

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_fingerprint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val errorHwNotFound = resources.getString(R.string.lock_fingerprint_error_hw_not_found)
        val errorAuthFail = resources.getString(R.string.lock_fingerprint_error_auth_fail)
        val errorNoFingerprints = resources.getString(R.string.lock_fingerprint_error_no_fingerprints)
        val errorBelowMarshmallow = resources.getString(R.string.lock_fingerprint_error_below_marshmallow)

        mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(context!!, object: FingerPrintAuthCallback {
            override fun onNoFingerPrintHardwareFound() {
                authFail.onNext(errorHwNotFound)
            }

            override fun onAuthFailed(errorCode: Int, errorMessage: String?) {
                authFail.onNext(errorAuthFail)
            }

            override fun onNoFingerPrintRegistered() {
                authFail.onNext(errorNoFingerprints)
            }

            override fun onBelowMarshmallow() {
                authFail.onNext(errorBelowMarshmallow)
            }

            override fun onAuthSuccess(cryptoObject: FingerprintManager.CryptoObject?) {
                authSuccess.onNext(Unit)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mFingerPrintAuthHelper.startAuth()
    }

    override fun onPause() {
        super.onPause()
        mFingerPrintAuthHelper.stopAuth()
    }

    override fun createPresenter() = FingerprintPresenter(
            Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<FingerprintModel>) {
        super.render(vs)

        if(vs.closeScreen){
            if(vs.model.unlockSuccess) unlockSuccess()
            else if(vs.model.usePin) usePinlock()
        }

        if(vs.model.unlockFail && vs.model.unlockFailMessage != null){
            tv_fingerprint_hint.text = vs.model.unlockFailMessage
            tv_fingerprint_hint.setTextColor(resources.getColor(R.color.error))
        }
    }

    private fun usePinlock(){
        (activity as? PinLockActivity)?.usePinlock()
    }

    private fun unlockSuccess() {
        (activity as? PinLockActivity)?.unlockSuccess()
    }
}