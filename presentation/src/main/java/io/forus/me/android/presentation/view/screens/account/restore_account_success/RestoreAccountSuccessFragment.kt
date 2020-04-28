package io.forus.me.android.presentation.view.screens.account.restore_account_success

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import io.forus.me.android.data.entity.common.BaseError
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.qr.dialogs.ScanVoucherBaseErrorDialog
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_account_restore_email.*
import kotlinx.android.synthetic.main.fragment_account_restore_email.restore
import kotlinx.android.synthetic.main.fragment_account_restore_email.root
import kotlinx.android.synthetic.main.fragment_account_restore_success.*
import java.lang.Exception


/**
 * Created by maestrovs on 22.04.2020.
 */
class RestoreAccountSuccessFragment : ToolbarLRFragment<RestoreAccountSuccessModel, RestoreAccountSuccessView, RestoreAccountSuccessPresenter>(), RestoreAccountSuccessView  {


    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"

        fun newIntent(token: String): RestoreAccountSuccessFragment = RestoreAccountSuccessFragment().also {
            val bundle = Bundle()
            bundle.putString(TOKEN_EXTRA, token)
            it.arguments = bundle
        }
    }

    private var token: String = ""



    private var instructionsAlreadyShown: Boolean = false

    override val toolbarTitle: String
        get() = ""//getString(R.string.restore_login)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_account_restore_success, container, false).also {

        val bundle = this.arguments
        if (bundle != null) {
            token = bundle.getString(TOKEN_EXTRA, "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun createPresenter() = RestoreAccountSuccessPresenter(
            token,
            Injection.instance.accountRepository
    )
    private var retrofitExceptionMapper: RetrofitExceptionMapper = Injection.instance.retrofitExceptionMapper


    override fun render(vs: LRViewState<RestoreAccountSuccessModel>) {
        super.render(vs)

        returnToRegistration.visibility = View.GONE


        progress.visibility = if(vs.model.sendingRestoreByEmail == true) View.VISIBLE else View.INVISIBLE

        if(vs.model.sendingRestoreByEmailSuccess == true && !instructionsAlreadyShown){

            navigator.navigateToCheckEmail(context!!)
        }

        if(vs.model.sendingRestoreByEmail == true){
            (activity as? BaseActivity)?.hideSoftKeyboard()
        }

        if (vs.model.accessToken != null && vs.model.accessToken.isNotBlank()) {
            successImage.visibility = View.VISIBLE
            title.text = getString(R.string.restore_account_head)
            nextStep.visibility = View.VISIBLE
        }else{
            successImage.visibility = View.INVISIBLE
            title.text = ""
            nextStep.visibility = View.INVISIBLE
        }


        if(vs.model.exchangeTokenError != null){
            errorImage.visibility = View.VISIBLE
            showToastMessage(resources.getString(R.string.restore_email_invalid_link))
            title.text = getString(R.string.restore_email_invalid_link)
        }else
        if(vs.loadingError != null){
            errorImage.visibility = View.VISIBLE
            var message = ""

            val error: Throwable = vs.loadingError
            if (error is RetrofitException && error.kind == RetrofitException.Kind.HTTP) {

                try {
                    val newRecordError = retrofitExceptionMapper.mapToBaseApiError(error)
                         message = if (newRecordError.message == null) "" else newRecordError.message

                } catch (e: Exception) {
                }
            }
            title.visibility = View.VISIBLE
            title.text = message

            returnToRegistration.visibility = View.VISIBLE

        }else{
            errorImage.visibility = View.INVISIBLE
        }




        returnToRegistration.setOnClickListener {
            navigator.navigateToLoginSignUp(activity)
            activity?.finish()
        }

        nextStep.setOnClickListener {
            if (vs.model.accessToken != null && vs.model.accessToken.isNotBlank()) {
                closeScreen(vs.model.accessToken)
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

