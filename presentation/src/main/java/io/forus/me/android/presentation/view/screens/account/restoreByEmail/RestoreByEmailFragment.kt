package io.forus.me.android.presentation.view.screens.account.restoreByEmail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.account.RestoreAccountByEmailRequest

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_restore_email_fragment.*

/**
 * Fragment New User Account Screen.
 */
class RestoreByEmailFragment : LRFragment<RestoreByEmailModel, RestoreByEmailView, RestoreByEmailPresenter>(), RestoreByEmailView  {


    private val viewIsValid: Boolean
        get() {
            var validation = email.validate() && email_repeat.validate()
            if (validation) {
                if (email.text.toString() != email_repeat.text.toString()) {
                    validation = false
                    email_repeat.error = "Email should be the same"
                }
            }
            return  validation
        }


    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }


    private val registerAction = PublishSubject.create<RestoreAccountByEmailRequest>()



    override fun register() = registerAction

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.account_restore_email_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        email.setText("test@test.com")
        email_repeat.setText("test@test.com")

        register.setOnClickListener {
            if (viewIsValid) {
                registerAction.onNext(RestoreAccountByEmailRequest(
                        email = email.text.toString()))
            }
        }
    }



    override fun createPresenter() = RestoreByEmailPresenter(
            Injection.instance.accountRepository
    )


    override fun render(vs: LRViewState<RestoreByEmailModel>) {
        super.render(vs)

        if (vs.closeScreen) {
            closeScreen()
        }



    }

    fun closeScreen() {
        activity?.finish()
    }


}

