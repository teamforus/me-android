package io.forus.me.android.presentation.view.screens.account.newaccount.pin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentAccountSetPinBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.view.component.pinlock.PinLockListener
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class NewPinFragment : ToolbarLRFragment<NewPinModel, NewPinView, NewPinPresenter>(), NewPinView ,
    MViewModelProvider<NewPinViewModel> {


     override val viewModel by lazy {
        ViewModelProvider(requireActivity())[NewPinViewModel::class.java].apply { }
    }


    companion object {
        private val ACCESS_TOKEN_EXTRA = "ACCESS_TOKEN_EXTRA"

        fun newIntent(accessToken: String): NewPinFragment = NewPinFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(ACCESS_TOKEN_EXTRA, accessToken)
            it.arguments = bundle
        }
    }

    override val showAccount: Boolean
        get() = false

    //private lateinit var accessToken: String

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    private val pinOnComplete = PublishSubject.create<String>()
    override fun pinOnComplete(): Observable<String> = pinOnComplete

    private val pinOnChange = PublishSubject.create<String>()
    override fun pinOnChange(): Observable<String> = pinOnChange

    private val skip = PublishSubject.create<Unit>()
    override fun skip(): Observable<Unit> = skip
    
    private lateinit var binding: FragmentAccountSetPinBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
       /* val bundle = this.arguments
        if (bundle != null) {
            bundle.getString(ACCESS_TOKEN_EXTRA)?.let {
                accessToken = it
            }?: kotlin.run {
                Log.e("","ACCESS_TOKEN_EXTRA is null")
            }
        }*/
        
        binding = FragmentAccountSetPinBinding.inflate(inflater)
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skip.onNext(Unit)
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots)
        binding.pinLockView.setPinLockListener(object: PinLockListener{
            override fun onComplete(pin: String?) {
                if(pin != null) pinOnComplete.onNext(pin)
            }

            override fun onEmpty() {
                pinOnChange.onNext("")
            }

            override fun onPinChange(pinLength: Int, intermediatePin: String?) {
                if(intermediatePin != null) pinOnChange.onNext(intermediatePin)
            }

        })

        binding.btnExit.setOnClickListener { skip.onNext(Unit) }
    }

    override fun createPresenter():NewPinPresenter {
        val accessToken = viewModel.accessToken.value?:""
        Log.d("PresenterR","createPresenter   accessToken = ${accessToken}")
         return NewPinPresenter(
            Injection.instance.accountRepository,
             accessToken
        )
    }

    override fun render(vs: LRViewState<NewPinModel>) {
        super.render(vs)

        binding.progressBar.visibility = if (vs.loading || vs.model.state == NewPinModel.State.CREATING_IDENTITY) View.VISIBLE else View.INVISIBLE
        binding.pinLockView.visibility = when (vs.model.state) { NewPinModel.State.CREATING_IDENTITY, NewPinModel.State.CREATING_IDENTITY_ERROR -> View.INVISIBLE else  -> View.VISIBLE}
        binding.indicatorDots.visibility = when (vs.model.state) { NewPinModel.State.CREATING_IDENTITY, NewPinModel.State.CREATING_IDENTITY_ERROR -> View.INVISIBLE else  -> View.VISIBLE}
        binding.btnExit.visibility = if(vs.model.skipEnabled) View.VISIBLE else View.INVISIBLE

        when(vs.model.state){
            NewPinModel.State.CREATE -> changeHeaders(resources.getString(R.string.passcode_title_create), resources.getString(R.string.passcode_subtitle_create), false)
            NewPinModel.State.CONFIRM -> changeHeaders(resources.getString(R.string.passcode_title_confirm), resources.getString(R.string.passcode_subtitle_create), false)
            NewPinModel.State.PASS_NOT_MATCH -> changeHeaders(resources.getString(R.string.passcode_title_create), resources.getString(R.string.passcode_subtitle_create_not_match), true)
            NewPinModel.State.CREATING_IDENTITY -> changeHeaders(resources.getString(R.string.passcode_title_create_identity_wait), resources.getString(R.string.passcode_subtitle_create_identity), false)
            NewPinModel.State.CREATING_IDENTITY_ERROR -> changeHeaders(resources.getString(R.string.passcode_subtitle_change_error), vs.model.createIdentityError?.message ?: "", true)
        }

        if(vs.model.state != vs.model.prevState) when(vs.model.state){
            NewPinModel.State.CONFIRM -> binding.pinLockView.resetPinLockView()
            NewPinModel.State.PASS_NOT_MATCH -> {
                binding.pinLockView.resetPinLockView()
                binding.pinLockView.setErrorAnimation()
            }
        }

        if (vs.closeScreen) {
            closeScreen()
        }
    }

    private fun changeHeaders(title: String, subtitle: String, error: Boolean){
        setToolbarTitle(title)
        if(binding.subtitleAction.text != subtitle) binding.subtitleAction.text = subtitle
        binding.subtitleAction.setTextColor(if(error) resources.getColor(R.color.error) else resources.getColor(R.color.body_1_87))
    }

    private fun closeScreen() {
        navigator.navigateToDashboard(activity)
        activity?.finish()
    }
}