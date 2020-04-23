package io.forus.me.android.presentation.view.base.lr

import android.support.annotation.CallSuper
import android.support.design.widget.Snackbar
import android.view.View
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.hannesdorfmann.mosby3.mvi.MviFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import android.widget.Toast
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.OnCompleteListener
import io.forus.me.android.presentation.navigation.Navigator

import android.R.id.message
import android.support.annotation.NonNull
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.view.base.lr.LRFragment
import io.forus.me.android.presentation.view.screens.account.account.dialogs.SessionExpiredDialog
import io.forus.me.android.presentation.view.screens.qr.dialogs.ScanVoucherNotEligibleDialog

import io.forus.me.android.data.exception.RetrofitException
import io.forus.me.android.presentation.view.base.NoInternetDialog



abstract class LRFragment<M, V : LRView<M>, P : MviBasePresenter<V, LRViewState<M>>> : MviFragment<V, P>(), LRView<M> {

    protected val navigator = Navigator()

    protected abstract fun viewForSnackbar(): View
    protected abstract fun loadRefreshPanel(): LoadRefreshPanel

    override fun retry(): Observable<Any> = loadRefreshPanel().retryClicks()
    override fun refresh(): Observable<Any> = loadRefreshPanel().refreshes()

    override fun updateData(): Observable<Any> {
        return updateObservable
    }

    protected fun showToastMessage(message: String) {
        val context = activity?.applicationContext
        if (context != null) Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun updateModel() {
        updateObservable.onNext(true)
    }

    override fun onResume() {
        super.onResume()
        mOnCompleteListener?.onResume()
    }

    open fun getScrollableView(): View? {
        return null
    }

    private val updateObservable = PublishSubject.create<Any>()


    @CallSuper
    override fun render(vs: LRViewState<M>) {
        loadRefreshPanel().render(vs)
        if (vs.refreshingError != null) {


            //401 Unauthorized
            if (vs.refreshingError.message != null && vs.refreshingError.message!!.contains("401 ")) {
                if (context != null) {
                    SessionExpiredDialog(context!!, MaterialDialog.SingleButtonCallback { _, _ ->
                        navigator.navigateToWelcomeScreen(activity)
                        activity?.finish()
                    }).show()
                }

            } else 
            if (vs.refreshingError is RetrofitException && vs.refreshingError.kind == io.forus.me.android.domain.exception.RetrofitException.Kind.NETWORK) {
                if (context != null) NoInternetDialog(context!!, {}).show();
            } else 
            {
                Snackbar.make(viewForSnackbar(), R.string.app_refreshing_error_text, Snackbar.LENGTH_SHORT).show()
            }



        }
    }

    var mOnCompleteListener: OnCompleteListener? = null

}


