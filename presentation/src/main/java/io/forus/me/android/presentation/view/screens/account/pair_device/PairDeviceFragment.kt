package io.forus.me.android.presentation.view.screens.account.pair_device

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.account.assigndelegates.AssignDelegatesAccountActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_account_assign_delegates.*
import kotlinx.android.synthetic.main.fragment_account_assign_delegates.pin_view
import kotlinx.android.synthetic.main.fragment_account_details.root
import kotlinx.android.synthetic.main.fragment_account_pair_device.*
import io.forus.me.android.presentation.view.screens.account.assigndelegates.qr.RestoreByQRFragment
import kotlinx.android.synthetic.main.toolbar_view.*


/**
 * Fragment User PairDevice Screen.
 */
class PairDeviceFragment : ToolbarLRFragment<PairDeviceModel, PairDeviceView, PairDevicePresenter>(), PairDeviceView {

    val disposableHolder = DisposableHolder()

    override fun viewForSnackbar(): View = root

    override val toolbarTitle: String
        get() = ""

    override val allowBack: Boolean
        get() = true

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_account_pair_device, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val qrFragment = RestoreByQRFragment()
            //val bundle = Bundle()
            //bundle.putString(KEY_MSG_1, "Заменили на первый фрагмент")
            //myFragment1.setArguments(bundle)

            val fragmentTransaction = fragmentManager!!
                    .beginTransaction()
            fragmentTransaction.replace(R.id.container_qr_fr, qrFragment,
                    null)
            fragmentTransaction.commit()

        //profile_button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.error_email))

        pin_view.setPinBackground(ContextCompat.getColor(context!!, R.color.pinBackground))
    }

    override fun onDetach() {
        super.onDetach()
        disposableHolder.disposeAll()
    }

    override fun createPresenter() = PairDevicePresenter(
            disposableHolder,
            Injection.instance.accessTokenChecker,
            Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<PairDeviceModel>) {
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
        navigator.navigateToResoreAccountSuccess(activity, accessToken, false)
        activity?.finish()
    }
}

