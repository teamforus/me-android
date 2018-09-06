package io.forus.me.android.presentation.view.screens.account.account.pin

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.models.ChangePinMode
import io.forus.me.android.presentation.view.component.pinlock.PinLockListener
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_set_pin_fragment.*

class ChangePinFragment : ToolbarLRFragment<ChangePinModel, ChangePinView, ChangePinPresenter>(), ChangePinView {

    companion object {
        private val MODE_EXTRA = "MODE_EXTRA";

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

    override fun viewForSnackbar(): View = root

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        = inflater.inflate(R.layout.account_set_pin_fragment, container, false).also {

        val bundle = this.arguments
        if (bundle != null) {
            mode = ChangePinMode.valueOf(bundle.getString(MODE_EXTRA))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pin_lock_view.attachIndicatorDots(indicator_dots)
        pin_lock_view.setPinLockListener(object: PinLockListener {
            override fun onComplete(pin: String?) {
                if(pin != null) pinOnComplete.onNext(pin)
            }

            override fun onEmpty() {}

            override fun onPinChange(pinLength: Int, intermediatePin: String?) {
                if(intermediatePin != null) pinOnChange.onNext(intermediatePin)
            }

        })
    }

    override fun createPresenter() = ChangePinPresenter(
            mode,
            Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<ChangePinModel>) {
        super.render(vs)

        progressBar.visibility = if (vs.loading || vs.model.state == ChangePinModel.State.CHECKING_OLD_PIN || vs.model.state == ChangePinModel.State.CHANGING_PIN) View.VISIBLE else View.INVISIBLE
        pin_lock_view.visibility = when (vs.model.state) { ChangePinModel.State.CHECKING_OLD_PIN, ChangePinModel.State.CHANGING_PIN, ChangePinModel.State.CHANGE_PIN_ERROR -> View.INVISIBLE else  -> View.VISIBLE}
        indicator_dots.visibility = when (vs.model.state) {ChangePinModel.State.CHECKING_OLD_PIN, ChangePinModel.State.CHANGING_PIN, ChangePinModel.State.CHANGE_PIN_ERROR -> View.INVISIBLE else  -> View.VISIBLE}

        when(vs.model.state){
            ChangePinModel.State.CONFIRM_OLD_PIN -> changeHeaders(resources.getString(R.string.subtitle_pinlock_confirm), "", false)
            ChangePinModel.State.CHECKING_OLD_PIN -> changeHeaders(resources.getString(R.string.subtitle_pinlock_checking), "", false)
            ChangePinModel.State.WRONG_OLD_PIN -> changeHeaders(resources.getString(R.string.subtitle_pinlock_confirm), resources.getString(R.string.subtitle_pinlock_error), true)
            ChangePinModel.State.CREATE_NEW_PIN -> changeHeaders(resources.getString(R.string.title_create_passcode), resources.getString(R.string.subtitle_create_passcode), false)
            ChangePinModel.State.CONFIRM_NEW_PIN -> changeHeaders(resources.getString(R.string.title_confirm_passcode), resources.getString(R.string.subtitle_create_passcode), false)
            ChangePinModel.State.PASS_NOT_MATCH -> changeHeaders(resources.getString(R.string.title_create_passcode), resources.getString(R.string.subtitle_create_passcode_not_match), true)
            ChangePinModel.State.CHANGING_PIN -> changeHeaders(resources.getString(R.string.title_create_identity), resources.getString(R.string.changing_passcoce), false)
            ChangePinModel.State.CHANGE_PIN_ERROR -> changeHeaders("", resources.getString(R.string.subtitle_create_identity_error), true)
        }

        if(vs.model.state != vs.model.prevState) when(vs.model.state){
            ChangePinModel.State.CREATE_NEW_PIN -> {
                if(vs.model.prevState != ChangePinModel.State.PASS_NOT_MATCH) pin_lock_view.resetPinLockView()
            }
            ChangePinModel.State.CONFIRM_NEW_PIN -> pin_lock_view.resetPinLockView()
            ChangePinModel.State.PASS_NOT_MATCH, ChangePinModel.State.WRONG_OLD_PIN -> {
                pin_lock_view.resetPinLockView()
                pin_lock_view.setErrorAnimation()
            }
        }

        if (vs.closeScreen) {
            closeScreen()
        }
    }

    private fun changeHeaders(title: String, subtitle: String, error: Boolean){
        setToolbarTitle(title)
        if(!subtitle_action.text.equals(subtitle)) subtitle_action.text = subtitle
        subtitle_action.setTextColor(if(error) resources.getColor(R.color.error) else resources.getColor(R.color.body_1_87))
    }

    private fun closeScreen() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }
}