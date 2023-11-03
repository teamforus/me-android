package io.forus.me.android.presentation.view.screens.account.assigndelegates.email

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentAccountRestoreEmailBinding
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/**
 * Fragment New User Account Screen.
 */
class RestoreByEmailFragment : ToolbarLRFragment<RestoreByEmailModel, RestoreByEmailView, RestoreByEmailPresenter>(), RestoreByEmailView,
    MViewModelProvider<RestoreByEmailViewModel> {

     override val viewModel by lazy {
        ViewModelProvider(requireActivity())[RestoreByEmailViewModel::class.java].apply { }
    }

    private lateinit var binding: FragmentAccountRestoreEmailBinding

    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"

        fun newIntent(token: String): RestoreByEmailFragment = RestoreByEmailFragment().also {
            val bundle = Bundle()
            bundle.putString(TOKEN_EXTRA, token)
            it.arguments = bundle
        }
    }

   // private var token: String = ""

    private val viewIsValid: Boolean
        get() {
            val validation = binding.email.validate() //&& email_repeat.validate()
//            if (validation) {
//                if (email.getText() != email_repeat.getText()) {
//                    validation = false
//                    email_repeat.setError("Emails should be the same")
//                }
//            }
            return  validation
        }

    private var instructionsAlreadyShown: Boolean = false

    override val toolbarTitle: String
        get() = getString(R.string.restore_login)

    override val allowBack: Boolean
        get() = true


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentAccountRestoreEmailBinding.inflate(inflater)
        return binding.root
    }
    
         

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.email.showError = false
        binding.restore.active = false

        val listener = object: android.text.TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.restore.active = viewIsValid
            }
        }

        binding.email.setTextChangedListener(listener)
        //email_repeat.setTextChangedListener(listener)

        binding.restore.setOnClickListener {
            binding.email.showError = true
            if (viewIsValid) {

                context?.let { it1 -> SharedPref.init(it1)
                    SharedPref.write(SharedPref.RESTORE_EMAIL, binding.email.getText());
                }

                registerAction.onNext(binding.email.getText())
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun createPresenter() = RestoreByEmailPresenter(
            viewModel.token.value?:"",
            Injection.instance.accountRepository
    )


    override fun render(vs: LRViewState<RestoreByEmailModel>) {
        super.render(vs)

        binding.restore.visibility = if(vs.model.sendingRestoreByEmail == true || vs.model.sendingRestoreByEmailSuccess == true) View.INVISIBLE else View.VISIBLE
        binding.emailDescription.visibility = if(vs.model.sendingRestoreByEmailSuccess == true) View.VISIBLE else View.INVISIBLE
        binding.email.isEditable = !(vs.model.sendingRestoreByEmailSuccess == true)

        if(vs.model.sendingRestoreByEmailSuccess == true && !instructionsAlreadyShown){

            navigator.navigateToCheckEmail(requireContext())
        }

        if(vs.model.sendingRestoreByEmail == true){
            (activity as? BaseActivity)?.hideSoftKeyboard()
        }

        if(vs.model.sendingRestoreByEmailError != null){
            binding.email.setError(resources.getString(R.string.restore_email_not_found))
        }

        if(vs.model.exchangeTokenError != null){
            showToastMessage(resources.getString(R.string.restore_email_invalid_link))
        }

        if (vs.model.accessToken != null && vs.model.accessToken.isNotBlank()) {
            closeScreen(vs.model.accessToken)
        }
    }

    fun closeScreen(accessToken: String) {
        //navigator.navigateToPinNew(activity, accessToken)
        navigator.navigateToResoreAccountSuccess(activity, accessToken, false)
        activity?.finish()
    }

    fun exchangeToken(token: String) {
        exchangeToken.onNext(token)
    }
}

