package io.forus.me.android.presentation.view.screens.account.newaccount

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentAccountNewBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Fragment New User Account Screen.
 */
class NewAccountFragment : ToolbarLRFragment<NewAccountModel, NewAccountView, NewAccountPresenter>(), NewAccountView  {


    private val viewIsValid: Boolean
        get() {
            var validation = binding.email.validate()
            if (validation) {
                if (binding.email.getText() != binding.emailRepeat.getText()) {
                    validation = false
                    binding.emailRepeat.setError(resources.getString(R.string.new_account_email_repeat_error))
                }
                else binding.emailRepeat.setError("")
            }
            else binding.emailRepeat.setError("")
            return  validation
        }

    private var showFieldErrors: Boolean = false
        set(value) {
            field = value
            binding.email.showError = value
            binding.emailRepeat.showError = value
            binding.firstName.showError = value
            binding.lastName.showError = value
            binding.bsn.showError = value
            binding.phone.showError = value
        }

    override val showAccount: Boolean
        get() = false


    override fun viewForSnackbar(): View = binding.root

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
    
    private lateinit var binding: FragmentAccountNewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentAccountNewBinding.inflate(inflater)
        return binding.root
    }
          

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showFieldErrors = false
        binding.register.active = false

        val listener = object: android.text.TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.register.active = viewIsValid
            }
        }

        binding.email.setTextChangedListener(listener)
        binding.emailRepeat.setTextChangedListener(listener)

        binding.register.setOnClickListener {
            (activity as? BaseActivity)?.hideSoftKeyboard()
            showFieldErrors = true
            if (viewIsValid) {
                registerAction.onNext(NewAccountRequest(
                        email = binding.email.getText()
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


        binding.progressBar.visibility = if (vs.loading || vs.model.sendingRegistration) View.VISIBLE else View.INVISIBLE

        if(vs.model.sendingRegistrationError != null) {
            val error: Throwable = vs.model.sendingRegistrationError
            val errorMessage = if(error is RetrofitException && error.kind == RetrofitException.Kind.HTTP) R.string.new_account_error_already_in_use else R.string.app_error_text
            Snackbar.make(viewForSnackbar(), errorMessage, Snackbar.LENGTH_SHORT).show()
        }

        if (vs.closeScreen && vs.model.isSuccess != null && vs.model.isSuccess) {

            navigator.navigateToCheckEmail(requireContext())
            activity?.finish()

        }
    }

    fun closeScreen(accessToken: String) {
        navigator.navigateToPinNew(activity, accessToken)
        activity?.finish()
    }
}

