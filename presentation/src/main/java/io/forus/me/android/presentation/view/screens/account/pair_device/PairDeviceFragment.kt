package io.forus.me.android.presentation.view.screens.account.pair_device

import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentAccountPairDeviceBinding
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.forus.me.android.presentation.view.screens.account.assigndelegates.qr.RestoreByQRFragment



/**
 * Fragment User PairDevice Screen.
 */
class PairDeviceFragment : ToolbarLRFragment<PairDeviceModel, PairDeviceView, PairDevicePresenter>(), PairDeviceView {

    val disposableHolder = DisposableHolder()

    override fun viewForSnackbar(): View = binding.root

    override val toolbarTitle: String
        get() = ""

    override val allowBack: Boolean
        get() = true

    override val showAccount: Boolean
        get() = false

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }
    
    private lateinit var binding: FragmentAccountPairDeviceBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentAccountPairDeviceBinding.inflate(inflater)
        return binding.root
    }        
    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val qrFragment = RestoreByQRFragment()
            //val bundle = Bundle()
            //bundle.putString(KEY_MSG_1, "Заменили на первый фрагмент")
            //myFragment1.setArguments(bundle)

            val fragmentTransaction = requireFragmentManager()
                    .beginTransaction()
            fragmentTransaction.replace(R.id.container_qr_fr, qrFragment,
                    null)
            fragmentTransaction.commit()

        //profile_button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.error_email))

        binding.pinView.setPinBackground(ContextCompat.getColor(context!!, R.color.pinBackground))
    }

    override fun onDetach() {
        super.onDetach()
        disposableHolder.disposeAll()
    }

    override fun createPresenter() = PairDevicePresenter(
            disposableHolder,
            Injection.instance.accessTokenChecker,
            Injection.instance.accountRepository
    )

    override fun render(vs: LRViewState<PairDeviceModel>) {
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
        navigator.navigateToResoreAccountSuccess(activity, accessToken, false)
        activity?.finish()
    }
}

