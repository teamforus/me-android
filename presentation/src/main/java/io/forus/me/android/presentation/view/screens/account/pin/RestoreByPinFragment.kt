package io.forus.me.android.presentation.view.screens.account.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_restore_pin_fragment.*

/**
 * Fragment Assign Delegates Screen.
 */
class RestoreByPinFragment : LRFragment<RestoreByPinModel, RestoreByPinView, RestoreByPinPresenter>(), RestoreByPinView  {


    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.account_restore_pin_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun createPresenter() = RestoreByPinPresenter(
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

    }
}

