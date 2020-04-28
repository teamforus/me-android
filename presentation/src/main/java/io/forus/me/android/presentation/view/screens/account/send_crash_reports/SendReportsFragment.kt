package io.forus.me.android.presentation.view.screens.account.send_crash_reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_send_reports.*


/**
 * Created by maestrovs on 22.04.2020.
 */
class SendReportsFragment : ToolbarLRFragment<SendReportsModel, SendReportsView, SendReportsPresenter>(), SendReportsView  {



    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"

        fun newIntent(token: String): SendReportsFragment = SendReportsFragment().also {
            val bundle = Bundle()
            bundle.putString(TOKEN_EXTRA, token)
            it.arguments = bundle
        }
    }

    private var token: String = ""




    override val toolbarTitle: String
        get() = ""

    override val allowBack: Boolean
        get() = true


    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    private val switchSendCrashReports = PublishSubject.create<Boolean>()
    override fun switchSendCrashReports(): Observable<Boolean> = switchSendCrashReports



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_send_reports, container, false).also {

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

    override fun createPresenter() = SendReportsPresenter(
            Injection.instance.accountRepository
    )



    override fun render(vs: LRViewState<SendReportsModel>) {
        super.render(vs)

        enable_send_crash_log.setChecked(vs.model.sendCrashReportsEnabled)
        enable_send_crash_log.setOnClickListener {

            if(enable_send_crash_log.isChecked){
                SharedPref.init(context!!)
                SharedPref.write(SharedPref.OPTION_SEND_CRASH_REPORT,true)
            }
            switchSendCrashReports.onNext(!vs.model.sendCrashReportsEnabled)
        }

        nextStep.setOnClickListener { closeScreen(token) }

    }

    fun closeScreen(accessToken: String) {
        navigator.navigateToPinNew(activity, accessToken)
        activity?.finish()
    }


}

