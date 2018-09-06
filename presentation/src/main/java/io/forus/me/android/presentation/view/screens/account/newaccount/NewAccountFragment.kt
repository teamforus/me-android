package io.forus.me.android.presentation.view.screens.account.newaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_new_fragment.*

/**
 * Fragment New User Account Screen.
 */
class NewAccountFragment : ToolbarLRFragment<NewAccountModel, NewAccountView, NewAccountPresenter>(), NewAccountView  {


    private val viewIsValid: Boolean
        get() {
            return email.validate() //firstName.validate() && lastName.validate() && bsn.validate() && email.validate() && phone.validate();
        }


    override val showAccount: Boolean
        get() = false


    override fun viewForSnackbar(): View = root

    override val toolbarTitle: String
        get() = getString(R.string.new_account_title)

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }


    private val registerAction = PublishSubject.create<NewAccountRequest>()
    override fun register() = registerAction

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.account_new_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register.setOnClickListener {
            if (viewIsValid) {
                registerAction.onNext(NewAccountRequest(
                        firstname = firstName.getTextOrNullIfBlank(),
                        lastname = lastName.getTextOrNullIfBlank(),
                        bsn = bsn.getTextOrNullIfBlank(),
                        phoneNumber = phone.getTextOrNullIfBlank(),
                        email = email.getText()
                        )
                )
            }
        }
    }


    override fun createPresenter() = NewAccountPresenter(
            Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<NewAccountModel>) {
        super.render(vs)

        progressBar.visibility = if (vs.loading || vs.model.sendingRegistration) View.VISIBLE else View.INVISIBLE

        if(vs.model.sendingRegistrationError != null) {
            val error: Throwable = vs.model.sendingRegistrationError
            showToastMessage(resources.getString(
                    if(error is RetrofitException && error.kind == RetrofitException.Kind.HTTP) R.string.new_account_error_already_in_use
                    else R.string.error_text)
            )
        }

        if (vs.closeScreen && vs.model.accessToken != null) {
            closeScreen(vs.model.accessToken)
        }
    }

    fun closeScreen(accessToken: String) {
        navigator.navigateToPinNew(activity, accessToken)
        activity?.finish()
    }
}

