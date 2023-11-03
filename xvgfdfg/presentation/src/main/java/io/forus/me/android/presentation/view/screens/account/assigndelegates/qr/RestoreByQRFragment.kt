package io.forus.me.android.presentation.view.screens.account.assigndelegates.qr

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRFragment
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.domain.models.qr.QrCode
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentPopupQrBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.reactivex.Observable

class RestoreByQRFragment : LRFragment<RestoreByQRModel, RestoreByQRView, RestoreByQRPresenter>(), RestoreByQRView  {

    var qrText : String = ""
        set(value) {
            if(field != value){
                field = value
                if (binding.qrImage != null) {
                    binding.qrImage.setQRText(QrCode(QrCode.Type.AUTH_TOKEN, value).toJson())
                }
            }
        }

    val disposableHolder = DisposableHolder()

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    private lateinit var binding: FragmentPopupQrBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentPopupQrBinding.inflate(inflater)
        return binding.root
    }


    override fun onDetach() {
        super.onDetach()
        disposableHolder.disposeAll()
    }

    override fun createPresenter() = RestoreByQRPresenter(
            disposableHolder,
            Injection.instance.accessTokenChecker,
            Injection.instance.accountRepository
    )


    override fun render(vs: LRViewState<RestoreByQRModel>) {
        super.render(vs)

        binding.subtitle.text = resources.getString(R.string.restore_scan_deze_qr)

        if (vs.model.item != null) {
            qrText = vs.model.item.authToken
        }

        if(vs.closeScreen && vs.model.isQrConfirmed == true && vs.model.item?.accessToken != null){
            closeScreen(vs.model.item.accessToken)
        }
    }

    fun closeScreen(accessToken: String) {
        navigator.navigateToPinNew(activity, accessToken)
        navigator.navigateToResoreAccountSuccess(activity, accessToken, false)//accessToken)
        activity?.finish()
    }
}