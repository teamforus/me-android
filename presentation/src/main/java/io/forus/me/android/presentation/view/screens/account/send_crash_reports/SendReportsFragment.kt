package io.forus.me.android.presentation.view.screens.account.send_crash_reports

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentSendReportsBinding
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
//import kotlinx.android.synthetic.main.fragment_send_reports.*


/**
 * Created by maestrovs on 22.04.2020.
 */
class SendReportsFragment :
    ToolbarLRFragment<SendReportsModel, SendReportsView, SendReportsPresenter>(), SendReportsView {


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


    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    private val switchSendCrashReports = PublishSubject.create<Boolean>()
    override fun switchSendCrashReports(): Observable<Boolean> = switchSendCrashReports


    private lateinit var binding: FragmentSendReportsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendReportsBinding.inflate(inflater)

        val bundle = this.arguments
        if (bundle != null) {
            token = bundle.getString(TOKEN_EXTRA, "")
        }

        val display = requireActivity().getWindowManager().getDefaultDisplay()
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = resources.displayMetrics.density
        val dpHeight = outMetrics.heightPixels / density
        val dpWidth = outMetrics.widthPixels / density
        //return if (dpWidth <= 320 || dpHeight < 522)
        //    inflater.inflate(R.layout.fragment_send_reports_nokia1, container, false)
        //else inflater.inflate(R.layout.fragment_send_reports, container, false)

        return binding.root

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
        Log.d("Popupp","Popupp render>>>")
        binding.enableSendCrashLog.setChecked(vs.model.sendCrashReportsEnabled)
        binding.enableSendCrashLog.setOnClickListener {

            if (binding.enableSendCrashLog.isChecked) {
                SharedPref.init(requireContext())
                SharedPref.write(SharedPref.OPTION_SEND_CRASH_REPORT, true)
            }
            switchSendCrashReports.onNext(!vs.model.sendCrashReportsEnabled)
        }

        binding.nextStep.setOnClickListener {
            Log.d("Popupp","Popupp NEXT>>>")
            closeScreen(token) }

    }

    fun closeScreen(accessToken: String) {
        navigator.navigateToPinNew(activity, accessToken)
        activity?.finish()
    }


}

