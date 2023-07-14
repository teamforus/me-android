package io.forus.me.android.presentation.view.screens.account.newaccount.confirmRegistration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.account.assigndelegates.email.RestoreByEmailModel
import io.forus.me.android.presentation.view.screens.account.assigndelegates.email.RestoreByEmailPresenter
import io.forus.me.android.presentation.view.screens.account.assigndelegates.email.RestoreByEmailView
import io.forus.me.android.presentation.view.screens.account.newaccount.pin.NewPinViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_confirm_registration.*



class ConfirmRegistrationFragment :  ToolbarLRFragment<ConfirmRegistrationModel, ConfirmRegistrationView, ConfirmRegistrationPresenter>(), ConfirmRegistrationView,
    MViewModelProvider<ConfirmRegistrationViewModel> {

    override val viewModel by lazy {
        ViewModelProvider(requireActivity())[ConfirmRegistrationViewModel::class.java].apply { }
    }

    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"

        fun newIntent(token: String): ConfirmRegistrationFragment = ConfirmRegistrationFragment().also {
            val bundle = Bundle()
            bundle.putString(TOKEN_EXTRA, token)
            it.arguments = bundle
        }
    }

  //  private var token: String = ""



    private var instructionsAlreadyShown: Boolean = false

    override val toolbarTitle: String
        get() = ""

    override val allowBack: Boolean
        get() = false


    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }



    private val exchangeToken = PublishSubject.create<String>()
    override fun exchangeToken() = exchangeToken

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_confirm_registration, container, false).also {

        val bundle = this.arguments
       // if (bundle != null) {
       //     token = bundle.getString(TOKEN_EXTRA, "")
      //  }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun createPresenter() = ConfirmRegistrationPresenter(
            viewModel.token.value?:"",
            Injection.instance.accountRepository
    )


    override fun render(vs: LRViewState<ConfirmRegistrationModel>) {
        super.render(vs)



        if(vs.model.exchangeTokenError != null){
            showToastMessage(resources.getString(R.string.restore_email_invalid_link))
        }

        if (vs.model.accessToken != null && vs.model.accessToken.isNotBlank()) {
            closeScreen(vs.model.accessToken)
        }
    }

    fun closeScreen(accessToken: String) {
        navigator.navigateToPinNew(activity, accessToken)
        activity?.finish()
    }

    fun exchangeToken(token: String) {
        exchangeToken.onNext(token)
    }
}

