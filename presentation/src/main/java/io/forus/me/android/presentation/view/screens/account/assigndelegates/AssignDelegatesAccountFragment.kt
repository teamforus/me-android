package io.forus.me.android.presentation.view.screens.account.assigndelegates

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel

import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.R.layout.account_assign_delegates_fragment
import io.forus.me.android.presentation.interfaces.SlidingToolbarFragmentActionListener
import io.forus.me.android.presentation.interfaces.SlidingToolbarFragmentListener
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_assign_delegates_fragment.*

/**
 * Fragment Assign Delegates Screen.
 */
class AssignDelegatesAccountFragment : LRFragment<AssignDelegatesModel, AssignDelegatesView, AssignDeligatesPresenter>(), AssignDelegatesView, SlidingToolbarFragmentListener  {


    override fun viewForSnackbar(): View = root

    private  var qrFragment: QrFragment =  QrFragment.newIntent()
    var slidingToolbarFragmentActionListener : SlidingToolbarFragmentActionListener? = null


    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.account_assign_delegates_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        via_email.setOnClickListener {
            navigator.navigateToAccountRestoreByEmail(activity)
        }

        via_pin.setOnClickListener {
            navigator.navigateToAccountRestoreByPin(activity)
        }

        show_qr_panel.setOnClickListener {
            slidingToolbarFragmentActionListener?.openPanel()
        }
    }


    override fun createPresenter() = AssignDeligatesPresenter(
            Injection.instance.accountRepository
    )


    override fun render(vs: LRViewState<AssignDelegatesModel>) {
        super.render(vs)


        if (vs.model.item != null)
            qrFragment.qrText = vs.model.item.address


    }


    override fun getSlidingFragment(): BaseFragment {
        return  qrFragment
    }

    override fun getSlidingPanelTitle(): String {
        return "QR code"
    }



}

