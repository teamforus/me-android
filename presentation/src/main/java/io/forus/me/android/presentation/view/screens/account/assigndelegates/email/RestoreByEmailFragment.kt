package io.forus.me.android.presentation.view.screens.account.assigndelegates.email

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_restore_email_fragment.*


/**
 * Fragment New User Account Screen.
 */
class RestoreByEmailFragment : ToolbarLRFragment<RestoreByEmailModel, RestoreByEmailView, RestoreByEmailPresenter>(), RestoreByEmailView  {

    val disposableHolder = DisposableHolder()

    private val viewIsValid: Boolean
        get() {
            var validation = email.validate() //&& email_repeat.validate()
//            if (validation) {
//                if (email.getText() != email_repeat.getText()) {
//                    validation = false
//                    email_repeat.setError("Emails should be the same")
//                }
//            }
            return  validation
        }

    override val toolbarTitle: String
        get() = getString(R.string.login)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.account_restore_email_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restore.active = false

        val listener = object: android.text.TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                restore.active = viewIsValid;
            }
        }

        email.setTextChangedListener(listener)
        //email_repeat.setTextChangedListener(listener)

        restore.setOnClickListener {
            if (viewIsValid) {
                registerAction.onNext(email.getText())
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        disposableHolder.disposeAll()
    }

    override fun createPresenter() = RestoreByEmailPresenter(
            disposableHolder,
            Injection.instance.accessTokenChecker,
            Injection.instance.accountRepository
    )


    override fun render(vs: LRViewState<RestoreByEmailModel>) {
        super.render(vs)

        restore.visibility = if(vs.model.sendingRestoreByEmail == true) View.INVISIBLE else View.VISIBLE
        email_description.visibility = if(vs.model.sendingRestoreByEmail == true) View.VISIBLE else View.INVISIBLE

        if(vs.model.sendingRestoreByEmail == true){
            hideSoftKeyboard()
        }

        if(vs.model.sendingRestoreByEmailError != null){
            email.setError("Identity not found")
        }

        if (vs.closeScreen && vs.model.isEmailConfirmed && vs.model.item?.accessToken != null) {
            closeScreen(vs.model.item.accessToken)
        }
    }

    fun closeScreen(accessToken: String) {
        navigator.navigateToPinNew(activity, accessToken)
        activity?.finish()
    }

    fun hideSoftKeyboard() {
        try {
            val view = activity?.getCurrentFocus()
            if (view != null) {
                val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

