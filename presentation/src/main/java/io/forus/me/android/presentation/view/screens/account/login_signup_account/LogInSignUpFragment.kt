package io.forus.me.android.presentation.view.screens.account.login_signup_account

import android.os.Bundle
import androidx.core.text.HtmlCompat
import android.text.Editable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import io.forus.me.android.domain.models.account.NewAccountRequest
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
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_account_details.root
import kotlinx.android.synthetic.main.view_edit_text.*


/**
 * Fragment User Account Screen.
 */
class LogInSignUpFragment : ToolbarLRFragment<LogInSignUpModel, LogInSignUpView, LogInSignUpPresenter>(), LogInSignUpView {


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

    private var token: String = ""

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


    private val registerAction = PublishSubject.create<String>()
    override fun register() = registerAction

    private val exchangeToken = PublishSubject.create<String>()
    override fun exchangeToken() = exchangeToken


    private val registerActionNewAccount = PublishSubject.create<NewAccountRequest>()
    override fun registerNewAccount() = registerActionNewAccount


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        val bundle = this.arguments
        if (bundle != null) {
            token = bundle.getString(TOKEN_EXTRA, "")
        }


        val display = activity!!.getWindowManager().getDefaultDisplay()
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

        restore!!.setOnClickListener {
            email!!.showError = true
            if (viewIsValid) {

                context?.let { it1 ->
                    SharedPref.init(it1)
                    SharedPref.write(SharedPref.RESTORE_EMAIL, email!!.getText());
                };

                registerAction.onNext(email!!.getText())
            }
        }

        pair_device!!.setOnClickListener {
            navigator.navigateToPairDevice(context!!)
        }


    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun createPresenter() = LogInSignUpPresenter(
            token,
            Injection.instance.accountRepository
    )


    override fun render(vs: LRViewState<LogInSignUpModel>) {
        super.render(vs)


        restore!!.isEnabled = vs.model.sendingRestoreByEmail != true

        pair_device!!.isEnabled = vs.model.sendingRestoreByEmail != true
        // email_description.visibility = if(vs.model.sendingRestoreByEmailSuccess == true) View.VISIBLE else View.INVISIBLE
        email!!.isEditable = vs.model.sendingRestoreByEmail != true //!(vs.model.sendingRestoreByEmailSuccess == true)

        if (vs.model.sendingRestoreByEmailSuccess == true && !instructionsAlreadyShown) {

            navigator.navigateToCheckEmail(context!!)
        }

        if (vs.model.sendingRestoreByEmail == true) {
            (activity as? BaseActivity)?.hideSoftKeyboard()
        }

        if (vs.model.sendingRestoreByEmailError != null) {
            //email.setError(resources.getString(R.string.restore_email_not_found))
            //entry to registration new account
            if (viewIsValid) {
                registerActionNewAccount.onNext(NewAccountRequest(
                        firstname = "",
                        lastname = "",
                        bsn = "",
                        phoneNumber = "",
                        email = email!!.getText()
                )
                )
            }
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

                MaterialDialog(context!!).show {
                    listItems(R.array.api_items) { dialog, index, text ->
                        val newApiType = ApiConfig.stringToApiType(text.toString())
                        devOptionsBt!!.text = newApiType.name

                        if (newApiType == ApiType.OTHER) {
                            MaterialDialog(context)
                                    .message {
                                        HtmlCompat.fromHtml(
                                                "API format: <br/><b>https://{address}/</b> <i><font color='#DCDCDC'>api/v1/...</font></i>",
                                                HtmlCompat.FROM_HTML_MODE_LEGACY)
                                    }
                                    .show {
                                        input { dialog, text ->
                                            val customApiStr = text.toString()
                                            storedOtherApiStr = customApiStr

                                            CheckApiPresenter(context).checkApi(customApiStr,
                                                    { result ->
                                                        run {
                                                            if (result) {
                                                                MaterialDialog(context)
                                                                        .message { "Success test API server" }
                                                                        .show {
                                                                    positiveButton(R.string.me_ok) { dialog ->
                                                                        MaterialDialog(context)
                                                                                .message { "You must restart the application for the changes to take effect" }
                                                                                .show {
                                                                            positiveButton(R.string.me_ok) { dialog ->
                                                                                SharedPref.write(SharedPref.OPTION_CUSTOM_API_URL, customApiStr)
                                                                                SharedPref.write(SharedPref.OPTION_API_TYPE, newApiType.name)
                                                                                ApiConfig.changeToCustomApi(customApiStr)
                                                                                Utils.instance.restartApp(context)
                                                                            }
                                                                                    negativeButton { cancel() }
                                                                        }
                                                                    }
                                                                }

                                                            } else {
                                                                MaterialDialog(context).message { "Error test API server" }
                                                                .show {
                                                                    negativeButton(R.string.cancel)
                                                                }
                                                            }
                                                        }
                                                    },
                                                    { throwable ->
                                                        run {
                                                            MaterialDialog(context).message { throwable.localizedMessage }
                                                                    .show {
                                                                        negativeButton(R.string.cancel)
                                                                    }
                                                        }
                                                    }
                                            )
                                        }
                                        positiveButton(R.string.submit)
                                    }


                        } else {
                            MaterialDialog(context)
                                    .message { "You must restart the application for the changes to take effect" }
                                    .show {
                                        positiveButton(R.string.me_ok) { dialog ->
                                            SharedPref.write(SharedPref.OPTION_API_TYPE, newApiType.name)
                                            ApiConfig.changeApi(newApiType)
                                            Utils.instance.restartApp(context)
                                        }
                                        negativeButton { cancel() }
                                    }
                        }
                    }
                }
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

