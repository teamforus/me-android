package io.forus.me.android.presentation.view.screens.account.assigndelegates.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRFragment
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_account_restore_pin.*

class RestoreByPinFragment : LRFragment<RestoreByPinModel, RestoreByPinView, RestoreByPinPresenter>(), RestoreByPinView  {

    val disposableHolder = DisposableHolder()

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_account_restore_pin, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_pin_instructions.setOnClickListener {
            if(activity != null) PinInstructionsDialog(activity!!).show()
        }
    }

    override fun onDetach() {
        super.onDetach()
        disposableHolder.disposeAll()
    }

    override fun createPresenter() = RestoreByPinPresenter(
            disposableHolder,
            Injection.instance.accessTokenChecker,
            Injection.instance.accountRepository
    )


    override fun render(vs: LRViewState<RestoreByPinModel>) {
        super.render(vs)

        if (vs.model.item != null) {
            pin_view.setPin(vs.model.item.authCode)
            pin_view.visibility = View.VISIBLE
        }
        else
            pin_view.visibility = View.INVISIBLE


        if(vs.closeScreen && vs.model.isPinConfirmed == true && vs.model.item?.accessToken != null){
            closeScreen(vs.model.item.accessToken)
        }
    }

    fun closeScreen(accessToken: String) {
        navigator.navigateToPinNew(activity, accessToken)
        activity?.finish()
    }
}

