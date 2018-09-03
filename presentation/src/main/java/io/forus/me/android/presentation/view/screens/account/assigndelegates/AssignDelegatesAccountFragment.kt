package io.forus.me.android.presentation.view.screens.account.assigndelegates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_assign_delegates_fragment.*

/**
 * Fragment Assign Delegates Screen.
 */
class AssignDelegatesAccountFragment : ToolbarLRFragment<Unit, AssignDelegatesView, AssignDeligatesPresenter>(), AssignDelegatesView{


    override fun viewForSnackbar(): View = root

    override val toolbarTitle: String
        get() = getString(R.string.login)

    override val allowBack: Boolean
        get() = true

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
            (activity as? AssignDelegatesAccountActivity)?.showPopupPinFragment()
        }

        show_qr_panel.setOnClickListener {
            (activity as? AssignDelegatesAccountActivity)?.showPopupQRFragment()
        }
    }

    override fun createPresenter() = AssignDeligatesPresenter()
}

