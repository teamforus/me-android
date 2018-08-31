package io.forus.me.android.presentation.view.screens.account.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_details.*

/**
 * Fragment New User Account Screen.
 */
class AccountFragment : ToolbarLRFragment<AccountModel, AccountView, AccountPresenter>(), AccountView  {



    override val allowBack: Boolean
        get() = true


    override fun viewForSnackbar(): View = lr_panel

    override val toolbarTitle: String
        get() = ""

    override fun loadRefreshPanel() = lr_panel



    private val logout = PublishSubject.create<Boolean>()
    override fun logout(): Observable<Boolean> = logout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.account_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout_view.setOnClickListener {
            logout.onNext(true)
        }
    }


    override fun createPresenter() = AccountPresenter(
            Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<AccountModel>) {
        super.render(vs)

        name.text = vs.model.account?.name
        email.text = vs.model.account?.email

        if (vs.model.navigateToWelcome) {
            navigator.navigateToWelcomeScreen(activity)
        }

    }

}

