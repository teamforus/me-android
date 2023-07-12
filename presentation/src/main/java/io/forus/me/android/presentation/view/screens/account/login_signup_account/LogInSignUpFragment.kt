package io.forus.me.android.presentation.view.screens.account.login_signup_account

import android.os.Bundle
import android.text.Editable
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.records.errors.BaseApiError
import io.forus.me.android.presentation.BuildConfig
import io.forus.me.android.presentation.api_config.ApiConfig
import io.forus.me.android.presentation.api_config.ApiType
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.api_config.Utils
import io.forus.me.android.presentation.api_config.check_api_status.CheckApiPresenter
import io.forus.me.android.presentation.api_config.dialogs.*
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.base.NoInternetDialog
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_account_details.root
import java.lang.Exception


/**
 * Fragment User Account Screen.
 */
class LogInSignUpFragment : ToolbarLRFragment<LogInSignUpModel, LogInSignUpView, LogInSignUpPresenter>(), LogInSignUpView,
    MViewModelProvider<LoginSignUpViewModel> {


    var clickLoginUserAction = false


    override val viewModel by lazy {
        ViewModelProvider(requireActivity())[LoginSignUpViewModel::class.java].apply { }
    }

    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"

        fun newIntent(token: String): LogInSignUpFragment = LogInSignUpFragment().also {
            val bundle = Bundle()
            bundle.putString(TOKEN_EXTRA, token)
            it.arguments = bundle
        }
    }

    var email: io.forus.me.android.presentation.view.component.editors.EditText? = null
    var restore: io.forus.me.android.presentation.view.component.buttons.Button? = null
    var pair_device: io.forus.me.android.presentation.view.component.buttons.Button? = null
    var devOptionsBt: io.forus.me.android.presentation.view.component.buttons.ButtonWhite? = null

  //  private var token: String = ""

    private val viewIsValid: Boolean
        get() {
            val validation = email!!.validate() //&& email_repeat.validate()
//            if (validation) {
//                if (email.getText() != email_repeat.getText()) {
//                    validation = false
//                    email_repeat.setError("Emails should be the same")
//                }
//            }
            return validation
        }

    private var instructionsAlreadyShown: Boolean = false

    override val toolbarTitle: String
        get() = getString(R.string.restore_login)

    override val allowBack: Boolean
        get() = true


    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }


    private val restoreAction = PublishSubject.create<String>()
    override fun register() = restoreAction

    private val exchangeToken = PublishSubject.create<String>()
    override fun exchangeToken() = exchangeToken


    private val registerActionNewAccount = PublishSubject.create<NewAccountRequest>()
    override fun registerNewAccount() = registerActionNewAccount


    /*override fun validateEmail(): Observable<String> {
        TODO("Not yet implemented")
    }*/

    private val validateEmail = PublishSubject.create<String>()
    override fun validateEmail() = validateEmail


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


       // val bundle = this.arguments
       // if (bundle != null) {
       //     token = bundle.getString(TOKEN_EXTRA, "")
       // }


        val display = requireActivity().windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = resources.displayMetrics.density
        val dpHeight = outMetrics.heightPixels / density
        val dpWidth = outMetrics.widthPixels / density
        val rootView = if (dpWidth <= 320 || dpHeight < 522)
            inflater.inflate(R.layout.fragment_login_sign_up_nokia1, container, false)
        else inflater.inflate(R.layout.fragment_login_sign_up, container, false)

        email = rootView.findViewById(R.id.email)
        restore = rootView.findViewById(R.id.restore)
        pair_device = rootView.findViewById(R.id.pair_device)
        devOptionsBt = rootView.findViewById(R.id.devOptionsBt)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email!!.showError = false
        restore!!.active = false


        val listener = object : android.text.TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                restore!!.active = viewIsValid
            }
        }

        email!!.setTextChangedListener(listener)
        //email_repeat.setTextChangedListener(listener)


        pair_device!!.setOnClickListener {
            navigator.navigateToPairDevice(requireContext())
        }


    }

    override fun createPresenter() = LogInSignUpPresenter(
            viewModel.token.value?:"",
            Injection.instance.accountRepository
    )



    private var retrofitExceptionMapper: RetrofitExceptionMapper = Injection.instance.retrofitExceptionMapper



    override fun render(vs: LRViewState<LogInSignUpModel>) {
        super.render(vs)

        restore!!.isEnabled = vs.model.sendingRestoreByEmail != true

        pair_device!!.isEnabled = vs.model.sendingRestoreByEmail != true
        // email_description.visibility = if(vs.model.sendingRestoreByEmailSuccess == true) View.VISIBLE else View.INVISIBLE
        email!!.isEditable = vs.model.sendingRestoreByEmail != true //!(vs.model.sendingRestoreByEmailSuccess == true)

        if (vs.model.sendingRestoreByEmailSuccess == true && !instructionsAlreadyShown  && clickLoginUserAction) {

            clickLoginUserAction = false

            navigator.navigateToCheckEmail(requireContext())
        }

        if (vs.model.sendingRestoreByEmail == true) {
            (activity as? BaseActivity)?.hideSoftKeyboard()
        }



        restore!!.setOnClickListener {
            clickLoginUserAction = true
            validateEmail.onNext(email!!.getText())

        }

        if (vs.model.validateEmail != null) {

            if (vs.model.validateEmail.valid) {

                context?.let { it1 ->
                    SharedPref.init(it1)
                    SharedPref.write(SharedPref.RESTORE_EMAIL, email!!.getText())
                }

                if (vs.model.validateEmail.used) {
                    restoreAction.onNext(email!!.getText())

                } else {
                    registerActionNewAccount.onNext(NewAccountRequest(
                            email = email!!.getText()
                    ))
                }

            } else {
                processError(Throwable("Invalid email"))
            }

        }

        if (vs.model.validateEmailError != null) {
            processError(vs.model.validateEmailError)
        }


        if (vs.model.sendingRestoreByEmailError != null) {
            processError(vs.model.sendingRestoreByEmailError)
        }



        if (vs.model.exchangeTokenError != null) {
            showToastMessage(resources.getString(R.string.restore_email_invalid_link))
        }

        if (vs.model.accessToken != null && vs.model.accessToken.isNotBlank()) {
            closeScreen(vs.model.accessToken)
        }


        if (BuildConfig.APPLICATION_ID.equals("io.forus.me")) {
            devOptionsBt!!.visibility = View.GONE
        } else {
            devOptionsBt!!.visibility = View.VISIBLE
            devOptionsBt!!.text = ApiConfig.getCurrentApiType().name

            val defaultApi = "https://api.forus.io/"
            var storedOtherApiStr = SharedPref.read(SharedPref.OPTION_CUSTOM_API_URL, defaultApi)
                    ?: defaultApi

            devOptionsBt!!.setOnClickListener {
                ChooseApiDialog(requireContext(), MaterialDialog.ListCallback { dialog, itemView, position, text ->

                    val newApiType = ApiConfig.stringToApiType(text.toString())
                    devOptionsBt!!.text = newApiType.name

                    if (newApiType == ApiType.OTHER) {

                        CustomApiDialog(requireContext(), storedOtherApiStr, MaterialDialog.InputCallback { _, input ->

                            val customApiStr = input.toString()
                            storedOtherApiStr = customApiStr

                            CheckApiPresenter(requireContext()).checkApi(customApiStr,
                                    { result ->
                                        run {
                                            if (result) {
                                                TestApiSuccessDialog(requireContext(), customApiStr) {
                                                    SaveApiAndRestartDialog(requireContext()) {
                                                        SharedPref.write(SharedPref.OPTION_CUSTOM_API_URL, customApiStr)
                                                        SharedPref.write(SharedPref.OPTION_API_TYPE, newApiType.name)
                                                        ApiConfig.changeToCustomApi(customApiStr)
                                                        Utils.instance.restartApp(requireContext())
                                                    }.show()
                                                }.show()
                                            } else {
                                                TestApiErrorDialog(requireContext(), "", {}).show()
                                            }
                                        }
                                    },
                                    { throwable ->
                                        run {
                                            TestApiErrorDialog(requireContext(), throwable.localizedMessage, {}).show()
                                        }
                                    }
                            )

                        }, {}, {}).show()
                    } else {
                        SaveApiAndRestartDialog(requireContext()) {
                            SharedPref.write(SharedPref.OPTION_API_TYPE, newApiType.name)
                            ApiConfig.changeApi(newApiType)
                            Utils.instance.restartApp(requireContext())
                        }.show()
                    }

                }) { }.show()
            }
        }
    }

    fun processError(error: Throwable){


        Log.d("forus", "sendingRestoreByEmailError... err = "+error)


        if (error is io.forus.me.android.data.exception.RetrofitException && error.kind == RetrofitException.Kind.NETWORK) {
            Log.d("forus", "sendingRestoreByEmailError... err 1 ")
            NoInternetDialog(requireContext()) { }.show()
        } else
            if (error is RetrofitException && error.kind == RetrofitException.Kind.HTTP) {
                Log.d("forus", "sendingRestoreByEmailError... err 2 ")
                try {
                    when (error.responseCode) {

                        403 -> {
                            val newRecordError : BaseApiError = retrofitExceptionMapper.mapToBaseApiError(error)
                            val title = if (newRecordError.message == null) "" else newRecordError.message
                            ErrorDialog(requireContext(),title,"").show()
                        }
                        422 -> {
                            val newRecordError = retrofitExceptionMapper.mapToApiError(error)
                            val title = if (newRecordError.message == null) "" else newRecordError.message
                            val message = if (newRecordError.message == null) "" else newRecordError.emailFormatted
                            ErrorDialog(requireContext(),title,message).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("forus","processError $e")
                }
            }else{
                if(clickLoginUserAction) {
                    clickLoginUserAction = false
                    navigator.navigateToAccountRestoreByEmail(requireContext())
                }

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

