package io.forus.me.android.presentation.view.screens.lock.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRFragment
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.component.pinlock.PinLockListener
import io.forus.me.android.presentation.view.screens.account.account.dialogs.LogoutDialog
import io.forus.me.android.presentation.view.screens.lock.PinLockActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_pinlock.*

class PinLockFragment : LRFragment<PinLockModel, PinLockView, PinLockPresenter>(), PinLockView {

    companion object {
        fun newIntent(): PinLockFragment = PinLockFragment()
    }

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

    private val logoutBtn = PublishSubject.create<Unit>()
    override fun logout(): Observable<Unit> = logoutBtn

    override fun exit(): Observable<Unit> = RxView.clicks(btn_exit).map { Unit }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_pinlock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout_button.setOnClickListener {
            showConfirmLogoutDialog()
        }

        pin_lock_view.attachIndicatorDots(indicator_dots)
        pin_lock_view.setPinLockListener(object : PinLockListener {
            override fun onComplete(pin: String?) {
                if (pin != null) pinOnComplete.onNext(pin)
            }

            override fun onEmpty() {}

            override fun onPinChange(pinLength: Int, intermediatePin: String?) {
                if (intermediatePin != null) pinOnChange.onNext(intermediatePin)
            }

        })
    }

    private fun showConfirmLogoutDialog() {
        LogoutDialog(context!!) { logoutBtn.onNext(Unit) }.show();
    }

    override fun createPresenter() = PinLockPresenter(
            Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<PinLockModel>) {
        super.render(vs)

        if (vs.exitIdentity) {
            //navigator.navigateToWelcomeScreen(context)
            navigator.navigateToLoginSignUp(context)
            activity?.finish()
        }

        progressBar.visibility = if (vs.loading || vs.model.state == PinLockModel.State.CHECKING) View.VISIBLE else View.INVISIBLE
        pin_lock_view.visibility = when (vs.model.state) {
            PinLockModel.State.CHECKING, PinLockModel.State.SUCCESS -> View.INVISIBLE
            else -> View.VISIBLE
        }
        indicator_dots.visibility = when (vs.model.state) {
            PinLockModel.State.CHECKING, PinLockModel.State.SUCCESS -> View.INVISIBLE
            else -> View.VISIBLE
        }

        when (vs.model.state) {
            PinLockModel.State.CONFIRM -> changeHeaders(resources.getString(R.string.passcode_subtitle_pinlock_confirm), false)
            PinLockModel.State.CHECKING -> changeHeaders(resources.getString(R.string.passcode_subtitle_pinlock_checking), false)
            PinLockModel.State.WRONG_PIN -> changeHeaders(resources.getString(R.string.passcode_subtitle_pinlock_error), true)
        }

        if (vs.model.state != vs.model.prevState) when (vs.model.state) {
            PinLockModel.State.WRONG_PIN -> {
                pin_lock_view.resetPinLockView()
                pin_lock_view.setErrorAnimation()
            }
        }

        if (vs.closeScreen) {
            closeScreen(vs.model.state == PinLockModel.State.SUCCESS)
        }
    }

    private fun changeHeaders(subtitle: String, error: Boolean) {
        if (!subtitle_action.text.equals(subtitle)) subtitle_action.text = subtitle
        subtitle_action.setTextColor(if (error) resources.getColor(R.color.error) else resources.getColor(R.color.body_1_87))
    }

    private fun closeScreen(success: Boolean) {
        if (success) (activity as? PinLockActivity)?.unlockSuccess()
        else {
            //navigator.navigateToWelcomeScreen(activity)
            navigator.navigateToLoginSignUp(activity)
            activity?.finish()
        }
    }
}