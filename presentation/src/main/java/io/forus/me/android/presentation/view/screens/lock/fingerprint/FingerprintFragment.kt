package io.forus.me.android.presentation.view.screens.lock.fingerprint

//import android.hardware.fingerprint.FingerprintManager

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.jakewharton.rxbinding2.view.RxView
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentFingerprintBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.lock.PinLockActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch


class FingerprintFragment :
    ToolbarLRFragment<FingerprintModel, FingerprintView, FingerprintPresenter>(), FingerprintView {

    private val promptManager by lazy {
        BiometricPromptManager(requireActivity())
    }

    companion object {
        fun newIntent(): FingerprintFragment = FingerprintFragment()
    }

    override val showAccount: Boolean
        get() = false


    override val allowBack: Boolean
        get() = false


    override val toolbarTitle: String
        get() = resources.getString(R.string.lock_fingerprint_title)

   // private lateinit var mFingerPrintAuthHelper: FingerPrintAuthHelper

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun exit(): Observable<Unit> = RxView.clicks(binding.btnExit).map { Unit }

    private val authSuccess = PublishSubject.create<Unit>()
    override fun authSuccess(): Observable<Unit> = authSuccess

    private val authFail = PublishSubject.create<String>()
    override fun authFail(): Observable<String> = authFail

    private lateinit var binding: FragmentFingerprintBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFingerprintBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val errorHwNotFound = resources.getString(R.string.lock_fingerprint_error_hw_not_found)
        val errorAuthFail = resources.getString(R.string.lock_fingerprint_error_auth_fail)
        val errorNoFingerprints =
            resources.getString(R.string.lock_fingerprint_error_no_fingerprints)
        val errorBelowMarshmallow =
            resources.getString(R.string.lock_fingerprint_error_below_marshmallow)

        lifecycleScope.launch {
            promptManager.promptResults.collect { result ->
                when(result) {
                    is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                        result.error
                    }
                    BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                        authFail.onNext(errorAuthFail)
                    }
                    BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                        authFail.onNext(errorNoFingerprints)
                    }
                    BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                        authSuccess.onNext(Unit)
                    }
                    BiometricPromptManager.BiometricResult.FeatureUnavailable -> {
                        authFail.onNext(errorHwNotFound)
                    }
                    BiometricPromptManager.BiometricResult.HardwareUnavailable -> {
                        authFail.onNext(errorHwNotFound)
                    }
                }
            }
        }
    }






    override fun onResume() {
        super.onResume()
        val uiHandler = Handler(Looper.getMainLooper())
        uiHandler.post {
            promptManager.showBiometricPrompt(
                title = resources.getString(R.string.lock_fingerprint_title),
                description = resources.getString(R.string.lock_fingerprint_subtitle),
            )
        }
    }




    override fun createPresenter() = FingerprintPresenter(
        Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<FingerprintModel>) {
        super.render(vs)

        if (vs.closeScreen) {
            if (vs.model.unlockSuccess) unlockSuccess()
            else if (vs.model.usePin) usePinlock()
        }

        if (vs.model.unlockFail && vs.model.unlockFailMessage != null) {
            binding.tvFingerprintHint.text = vs.model.unlockFailMessage
            binding.tvFingerprintHint.setTextColor(resources.getColor(R.color.error))
        }
    }

    private fun usePinlock() {
        (activity as? PinLockActivity)?.usePinlock()
    }

    private fun unlockSuccess() {
        (activity as? PinLockActivity)?.unlockSuccess()
    }
}