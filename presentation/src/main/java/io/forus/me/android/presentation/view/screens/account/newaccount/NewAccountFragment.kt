package io.forus.me.android.presentation.view.screens.account.newaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.domain.models.account.NewAccountRequest

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_new_fragment.*

/**
 * Fragment New User Account Screen.
 */
class NewAccountFragment : LRFragment<NewAccountModel, NewAccountView, NewAccountPresenter>(), NewAccountView  {


    private val viewIsValid: Boolean
        get() {
            return email.validate() //firstName.validate() && lastName.validate() && bsn.validate() && email.validate() && phone.validate();
        }


    override fun viewForSnackbar(): View = root

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
                        email = email.text.toString()))
//                        lastname = lastName.text.toString(),
//                        bsn = bsn.text.toString(),
//                        email = email.text.toString(),
//                        phoneNumber = phone.text.toString()))
            }
        }
    }



    override fun createPresenter() = NewAccountPresenter(
            Injection.instance.accountRepository
    )


    override fun render(vs: LRViewState<NewAccountModel>) {
        super.render(vs)

        if (vs.closeScreen) {
            closeScreen()
        }



    }

    fun closeScreen() {
        navigator.navigateToDashboard(activity)
        activity?.finish()
    }


}

