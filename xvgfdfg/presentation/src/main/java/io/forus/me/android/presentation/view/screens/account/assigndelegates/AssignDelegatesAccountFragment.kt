package io.forus.me.android.presentation.view.screens.account.assigndelegates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentAccountAssignDelegatesBinding
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable

/**
 * Fragment Assign Delegates Screen.
 */
class AssignDelegatesAccountFragment : ToolbarLRFragment<AssignDelegatesAccountModel, AssignDelegatesView, AssignDelegatesPresenter>(), AssignDelegatesView{

    val disposableHolder = DisposableHolder()

    override fun viewForSnackbar(): View = binding.root

    override val toolbarTitle: String
        get() = getString(R.string.restore_login)

    override val allowBack: Boolean
        get() = true

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }
    
    private lateinit var binding: FragmentAccountAssignDelegatesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentAccountAssignDelegatesBinding.inflate(inflater)
        return binding.root
    }
           


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viaEmail.setOnClickListener {
            navigator.navigateToAccountRestoreByEmail(activity)
        }

        binding.viaQr.setOnClickListener {
            (activity as? AssignDelegatesAccountActivity)?.showPopupQRFragment()
        }
    }

    override fun onDetach() {
        super.onDetach()
        disposableHolder.disposeAll()
    }

    override fun createPresenter() = AssignDelegatesPresenter(
            disposableHolder,
            Injection.instance.accessTokenChecker,
            Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<AssignDelegatesAccountModel>) {
        super.render(vs)

        if (vs.model.item != null) {
            binding.pinView.setPin(vs.model.item.authCode)
            binding.pinView.visibility = View.VISIBLE
        }
        else
            binding.pinView.visibility = View.INVISIBLE


        if(vs.closeScreen && vs.model.isPinConfirmed == true && vs.model.item?.accessToken != null){
            closeScreen(vs.model.item.accessToken)
        }
    }

    fun closeScreen(accessToken: String) {
        navigator.navigateToPinNew(activity, accessToken)
        activity?.finish()
    }
}

