package io.forus.me.android.presentation.view.screens.account.newaccount.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.component.pinlock.PinLockListener
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_create_pin_fragment.*

class NewPinFragment : ToolbarLRFragment<NewPinModel, NewPinView, NewPinPresenter>(), NewPinView {

    companion object {
        private val ACCESS_TOKEN_EXTRA = "ACCESS_TOKEN_EXTRA";

        fun newIntent(accessToken: String): NewPinFragment = NewPinFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(ACCESS_TOKEN_EXTRA, accessToken)
            it.arguments = bundle
        }
    }

    private lateinit var accessToken: String

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.account_create_pin_fragment, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            accessToken = bundle.getString(ACCESS_TOKEN_EXTRA)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pin_lock_view.attachIndicatorDots(indicator_dots)
        pin_lock_view.setPinLockListener(object: PinLockListener{
            override fun onComplete(pin: String?) {
                if(pin != null) pinOnComplete.onNext(pin)
            }

            override fun onEmpty() {}

            override fun onPinChange(pinLength: Int, intermediatePin: String?) {
                if(intermediatePin != null) pinOnChange.onNext(intermediatePin)
            }

        })
    }

    override fun createPresenter() = NewPinPresenter(
            Injection.instance.accountRepository,
            accessToken
    )

    override fun render(vs: LRViewState<NewPinModel>) {
        super.render(vs)

        progressBar.visibility = if (vs.loading || vs.model.state == NewPinModel.State.CREATING_IDENTITY) View.VISIBLE else View.INVISIBLE
        pin_lock_view.visibility = when (vs.model.state) { NewPinModel.State.CREATING_IDENTITY, NewPinModel.State.CREATING_IDENTITY_ERROR -> View.INVISIBLE else  -> View.VISIBLE}
        indicator_dots.visibility = when (vs.model.state) { NewPinModel.State.CREATING_IDENTITY, NewPinModel.State.CREATING_IDENTITY_ERROR -> View.INVISIBLE else  -> View.VISIBLE}

        when(vs.model.state){
            NewPinModel.State.CREATE -> changeHeaders(resources.getString(R.string.title_create_passcode), resources.getString(R.string.subtitle_create_passcode), false)
            NewPinModel.State.CONFIRM -> changeHeaders(resources.getString(R.string.title_confirm_passcode), resources.getString(R.string.subtitle_create_passcode), false)
            NewPinModel.State.PASS_NOT_MATCH -> changeHeaders(resources.getString(R.string.title_create_passcode), resources.getString(R.string.subtitle_create_passcode_not_match), true)
            NewPinModel.State.CREATING_IDENTITY -> changeHeaders(resources.getString(R.string.title_create_identity), resources.getString(R.string.subtitle_create_identity), false)
            NewPinModel.State.CREATING_IDENTITY_ERROR -> changeHeaders("", resources.getString(R.string.subtitle_create_identity_error), true)
        }

        if(vs.model.state != vs.model.prevState) when(vs.model.state){
            NewPinModel.State.CONFIRM -> pin_lock_view.resetPinLockView()
            NewPinModel.State.PASS_NOT_MATCH -> {
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
        navigator.navigateToDashboard(activity, false)
        activity?.finish()
    }
}