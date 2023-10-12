package io.forus.me.android.presentation.view.screens.account.account.pin

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentAccountSetPinBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.models.ChangePinMode
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.component.pinlock.PinLockListener
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ChangePinFragment : ToolbarLRFragment<ChangePinModel, ChangePinView, ChangePinPresenter>(),
    ChangePinView, MViewModelProvider<ChangePinViewModel> {

    override val viewModel by lazy {
        ViewModelProvider(requireActivity())[ChangePinViewModel::class.java].apply { }
    }

    companion object {
        private val MODE_EXTRA = "MODE_EXTRA"

        fun newIntent(mode: ChangePinMode): ChangePinFragment = ChangePinFragment().also {
            val bundle = Bundle()
            bundle.putString(MODE_EXTRA, mode.name)
            it.arguments = bundle
        }
    }

    private lateinit var mode: ChangePinMode

    override val showAccount: Boolean
        get() = false

    override val allowBack: Boolean
        get() = true

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    private val pinOnComplete = PublishSubject.create<String>()
    override fun pinOnComplete(): Observable<String> = pinOnComplete

    private val pinOnChange = PublishSubject.create<String>()
    override fun pinOnChange(): Observable<String> = pinOnChange

    private lateinit var binding: FragmentAccountSetPinBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAccountSetPinBinding.inflate(inflater)

        val bundle = this.arguments
        if (bundle != null) {
            bundle.getString(MODE_EXTRA)?.let {
                mode = ChangePinMode.valueOf(it)
            }
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnExit.visibility = View.INVISIBLE

        binding.pinLockView.attachIndicatorDots(binding.indicatorDots)
        binding.pinLockView.setPinLockListener(object : PinLockListener {
            override fun onComplete(pin: String?) {
                if (pin != null) pinOnComplete.onNext(pin)
            }

            override fun onEmpty() {}

            override fun onPinChange(pinLength: Int, intermediatePin: String?) {
                if (intermediatePin != null) pinOnChange.onNext(intermediatePin)
            }

        })
    }

    override fun createPresenter() = ChangePinPresenter(
        viewModel.changePinMode.value ?: ChangePinMode.SET_NEW,
        Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<ChangePinModel>) {
        super.render(vs)

        binding.progressBar.visibility =
            if (vs.loading || vs.model.state == ChangePinModel.State.CHECKING_OLD_PIN || vs.model.state == ChangePinModel.State.CHANGING_PIN) View.VISIBLE else View.INVISIBLE
        binding.pinLockView.visibility = when (vs.model.state) {
            ChangePinModel.State.CHECKING_OLD_PIN, ChangePinModel.State.CHANGING_PIN, ChangePinModel.State.CHANGE_PIN_ERROR -> View.INVISIBLE
            else -> View.VISIBLE
        }
        binding.indicatorDots.visibility = when (vs.model.state) {
            ChangePinModel.State.CHECKING_OLD_PIN, ChangePinModel.State.CHANGING_PIN, ChangePinModel.State.CHANGE_PIN_ERROR -> View.INVISIBLE
            else -> View.VISIBLE
        }

        when (vs.model.state) {
            ChangePinModel.State.CONFIRM_OLD_PIN -> changeHeaders(
                resources.getString(R.string.passcode_title_confirm),
                resources.getString(R.string.passcode_subtitle_confirm),
                false
            )
            ChangePinModel.State.CHECKING_OLD_PIN -> changeHeaders(
                resources.getString(R.string.passcode_subtitle_pinlock_checking),
                "",
                false
            )
            ChangePinModel.State.WRONG_OLD_PIN -> changeHeaders(
                resources.getString(R.string.passcode_subtitle_pinlock_confirm),
                resources.getString(R.string.passcode_subtitle_pinlock_error),
                true
            )
            ChangePinModel.State.CREATE_NEW_PIN -> changeHeaders(
                resources.getString(R.string.passcode_title_create),
                resources.getString(R.string.passcode_subtitle_create),
                false
            )
            ChangePinModel.State.CONFIRM_NEW_PIN -> changeHeaders(
                resources.getString(R.string.passcode_title_new_confirm),
                resources.getString(R.string.passcode_subtitle_new_confirm),
                false
            )
            ChangePinModel.State.PASS_NOT_MATCH -> changeHeaders(
                resources.getString(R.string.passcode_title_create),
                resources.getString(R.string.passcode_subtitle_create_not_match),
                true
            )
            ChangePinModel.State.CHANGING_PIN -> changeHeaders(
                resources.getString(R.string.passcode_title_create_identity_wait),
                resources.getString(R.string.passcode_changing),
                false
            )
            ChangePinModel.State.CHANGE_PIN_ERROR -> changeHeaders(
                "",
                resources.getString(R.string.passcode_subtitle_change_error),
                true
            )
        }

        if (vs.model.state != vs.model.prevState) when (vs.model.state) {
            ChangePinModel.State.CREATE_NEW_PIN -> {
                if (vs.model.prevState != ChangePinModel.State.PASS_NOT_MATCH) binding.pinLockView.resetPinLockView()
            }
            ChangePinModel.State.CONFIRM_NEW_PIN -> binding.pinLockView.resetPinLockView()
            ChangePinModel.State.PASS_NOT_MATCH, ChangePinModel.State.WRONG_OLD_PIN -> {
                binding.pinLockView.resetPinLockView()
                binding.pinLockView.setErrorAnimation()
            }
        }

        if (vs.closeScreen) {
            closeScreen()
        }
    }

    private fun changeHeaders(title: String, subtitle: String, error: Boolean) {
        setToolbarTitle(title)
        if (!binding.subtitleAction.text.equals(subtitle)) binding.subtitleAction.text = subtitle
        binding.subtitleAction.setTextColor(
            if (error) resources.getColor(R.color.error) else resources.getColor(
                R.color.body_1_87
            )
        )
    }

    private fun closeScreen() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }
}