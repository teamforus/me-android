package io.forus.me.android.presentation.view.screens.account.restore_account_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentAccountRestoreSuccessBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RestoreAccountExchangeFragment :
    ToolbarLRFragment<RestoreAccountExchangeModel, RestoreAccountExchangeView, RestoreAccountExchangePresenter>(),
    RestoreAccountExchangeView,
    MViewModelProvider<RestoreAccountExchangeViewModel> {

    override val viewModel by lazy {
        ViewModelProvider(requireActivity())[RestoreAccountExchangeViewModel::class.java].apply { }
    }

    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"

        fun newIntent(token: String): RestoreAccountExchangeFragment = RestoreAccountExchangeFragment().also {
            val bundle = Bundle()
            bundle.putString(TOKEN_EXTRA, token)
            it.arguments = bundle
        }
    }

    private var exchangeCompleted = false
    private var errorAlreadyShown = false

    override val toolbarTitle: String
        get() = ""

    override val allowBack: Boolean
        get() = false

    override val showAccount: Boolean
        get() = false

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {
        }
    }

    private val exchangeToken = PublishSubject.create<String>()
    override fun exchangeToken() = exchangeToken

    private lateinit var binding: FragmentAccountRestoreSuccessBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentAccountRestoreSuccessBinding.inflate(inflater)
        return binding.root
    }

    override fun createPresenter() = RestoreAccountExchangePresenter(
        viewModel.token.value ?: arguments?.getString(TOKEN_EXTRA) ?: "",
        Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<RestoreAccountExchangeModel>) {
        super.render(vs)

        val isLoading = vs.loading || vs.model.exchangingToken == true
        val hasError = vs.loadingError != null || vs.model.exchangeTokenError != null

        binding.progress.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        binding.successImage.visibility = View.INVISIBLE
        binding.nextStep.visibility = View.GONE
        binding.errorImage.visibility = if (hasError) View.VISIBLE else View.INVISIBLE
        binding.returnToRegistration.visibility = if (hasError) View.VISIBLE else View.GONE
        binding.returnToRegistration.text = getString(R.string.welcome_login)
        binding.title.text = if (hasError) getString(R.string.restore_email_invalid_link) else ""

        binding.returnToRegistration.setOnClickListener {
            navigator.navigateToLoginSignUp(activity)
            activity?.finish()
        }

        if (isLoading) {
            errorAlreadyShown = false
        }

        if (hasError && !errorAlreadyShown) {
            errorAlreadyShown = true
            showToastMessage(resources.getString(R.string.restore_email_invalid_link))
        }

        if (!exchangeCompleted && vs.model.accessToken != null && vs.model.accessToken.isNotBlank()) {
            exchangeCompleted = true
            closeScreen(vs.model.accessToken)
        }
    }

    fun closeScreen(accessToken: String) {
        navigator.navigateToResoreAccountSuccess(activity, accessToken, false)
        activity?.finish()
    }

    fun exchangeToken(token: String) {
        exchangeCompleted = false
        exchangeToken.onNext(token)
    }
}
