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
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.lock.PinLockActivity
import io.reactivex.Observable
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_fingerprint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(context!!, object: FingerPrintAuthCallback {
            override fun onNoFingerPrintHardwareFound() {

            }

            override fun onAuthFailed(errorCode: Int, errorMessage: String?) {

            }

            override fun onNoFingerPrintRegistered() {

            }

            override fun onBelowMarshmallow() {

            }

            override fun onAuthSuccess(cryptoObject: FingerprintManager.CryptoObject?) {

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

    )

    override fun render(vs: LRViewState<FingerprintModel>) {
        super.render(vs)

        if(vs.closeScreen){
            if(vs.model.unlockSuccess) unlockSuccess()
            else if(vs.model.usePin) usePinlock()
        }
    }

    private fun usePinlock(){
        (activity as? PinLockActivity)?.usePinlock()
    }

    private fun unlockSuccess() {
        (activity as? PinLockActivity)?.unlockSuccess()
    }
}