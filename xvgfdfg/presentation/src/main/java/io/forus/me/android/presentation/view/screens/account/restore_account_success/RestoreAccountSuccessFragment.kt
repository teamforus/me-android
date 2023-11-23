package io.forus.me.android.presentation.view.screens.account.restore_account_success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentAccountRestoreSuccessBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject



/**
 * Created by maestrovs on 22.04.2020.
 */
class RestoreAccountSuccessFragment : ToolbarLRFragment<RestoreAccountSuccessModel, RestoreAccountSuccessView, RestoreAccountSuccessPresenter>(), RestoreAccountSuccessView,
    MViewModelProvider<RestoreAccountSuccessViewModel> {

    override val viewModel by lazy {
        ViewModelProvider(requireActivity())[RestoreAccountSuccessViewModel::class.java].apply { }
    }

    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"
        private val IS_EXCHANGE_TOKEN = "IS_EXCHANGE_TOKEN"

        fun newIntent(token: String, isExchangeToken: Boolean): RestoreAccountSuccessFragment = RestoreAccountSuccessFragment().also {
            val bundle = Bundle()
            bundle.putString(TOKEN_EXTRA, token)
            bundle.putBoolean(IS_EXCHANGE_TOKEN, isExchangeToken)
            it.arguments = bundle
        }
    }

    private var token: String = ""
    private var isExchangeToken: Boolean = true


    private var instructionsAlreadyShown: Boolean = false

    override val toolbarTitle: String
        get() = ""//getString(R.string.restore_login)

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

    private val registerAction = PublishSubject.create<String>()
    override fun register() = registerAction

    private val exchangeToken = PublishSubject.create<String>()
    override fun exchangeToken() = exchangeToken

    private lateinit var binding: FragmentAccountRestoreSuccessBinding
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentAccountRestoreSuccessBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        token = viewModel.token.value?:""
        isExchangeToken = viewModel.isExchangeToken.value?:true
        
    }

    override fun createPresenter() = RestoreAccountSuccessPresenter(
            viewModel.token.value?:"",
            Injection.instance.accountRepository,
        viewModel.isExchangeToken.value?:true
    )

    private var retrofitExceptionMapper: RetrofitExceptionMapper = Injection.instance.retrofitExceptionMapper


    override fun render(vs: LRViewState<RestoreAccountSuccessModel>) {
        super.render(vs)

        if (isExchangeToken) {

            binding.returnToRegistration.visibility = View.GONE


            binding.progress.visibility = if (vs.model.sendingRestoreByEmail == true) View.VISIBLE else View.INVISIBLE

            if (vs.model.sendingRestoreByEmailSuccess == true && !instructionsAlreadyShown) {

                navigator.navigateToCheckEmail(requireContext())
            }

            if (vs.model.sendingRestoreByEmail == true) {
                (activity as? BaseActivity)?.hideSoftKeyboard()
            }

            if (vs.model.accessToken != null && vs.model.accessToken.isNotBlank()) {
                binding.successImage.visibility = View.VISIBLE
                binding.title.text = getString(R.string.restore_account_head)
                binding.nextStep.visibility = View.VISIBLE
            } else {
                binding.successImage.visibility = View.INVISIBLE
                binding.title.text = ""
                binding.nextStep.visibility = View.INVISIBLE
            }


            if (vs.model.exchangeTokenError != null) {
                binding.errorImage.visibility = View.VISIBLE
                showToastMessage(resources.getString(R.string.restore_email_invalid_link))
                binding.title.text = getString(R.string.restore_email_invalid_link)
            } else
                if (vs.loadingError != null) {
                    binding.errorImage.visibility = View.VISIBLE
                    var message = ""

                    val error: Throwable = vs.loadingError
                    if (error is RetrofitException && error.kind == RetrofitException.Kind.HTTP) {

                        try {
                            val newRecordError = retrofitExceptionMapper.mapToBaseApiError(error)
                            message = if (newRecordError.message == null) "" else newRecordError.message

                        } catch (e: Exception) {
                        }
                    }
                    binding.title.visibility = View.VISIBLE
                    binding.title.text = message

                    binding.returnToRegistration.visibility = View.VISIBLE

                } else {
                    binding.errorImage.visibility = View.INVISIBLE
                }




            binding.returnToRegistration.setOnClickListener {
                navigator.navigateToLoginSignUp(activity)
                activity?.finish()
            }



            binding.nextStep.setOnClickListener {
                if (vs.model.accessToken != null && vs.model.accessToken.isNotBlank()) {
                    closeScreen(vs.model.accessToken)
                }
            }

        } else {
            binding.progress.visibility = View.GONE
            binding.successImage.visibility = View.VISIBLE
            binding.returnToRegistration.visibility = View.GONE
            binding.errorImage.visibility = View.GONE
            binding.title.text = getString(R.string.restore_account_head)
            binding.nextStep.visibility = View.VISIBLE
            binding.nextStep.setOnClickListener {
                if (token.isNotBlank()) {
                    closeScreen(token)
                }
            }
        }
    }

    fun closeScreen(accessToken: String) {
        navigator.navigateToSendReports(activity, accessToken)
        activity?.finish()
    }

    fun exchangeToken(token: String) {
        exchangeToken.onNext(token)
    }
}

