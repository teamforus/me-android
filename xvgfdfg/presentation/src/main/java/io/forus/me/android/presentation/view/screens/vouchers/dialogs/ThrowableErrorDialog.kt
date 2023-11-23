package io.forus.me.android.presentation.view.screens.vouchers.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection


class ThrowableErrorDialog(private val error: Throwable, private val context: Context,
                           private val dismissListener: () -> Unit){

    private var message = ""
    private var retrofitExceptionMapper: RetrofitExceptionMapper = Injection.instance.retrofitExceptionMapper

    init {
        if (error is RetrofitException) {

            if (error.responseCode == 422) {
                val detailsApiError = retrofitExceptionMapper.mapToDetailsApiError(error)
                if (detailsApiError != null && detailsApiError.errors != null) {
                    message = detailsApiError.errorString
                }
            }
            if (error.responseCode == 403) {
                val baseError = retrofitExceptionMapper.mapToBaseApiError(error)
                if (baseError != null && baseError.message != null) {
                    message = baseError.message
                }
            }
        }

       // message = error
    }


    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(R.string.qr_popup_error)
            .content(message)
            .icon(context.resources.getDrawable(R.drawable.ic_close))
            .positiveText(context.resources.getString(R.string.me_ok))
            .dismissListener { dismissListener.invoke() }
            .build()

    fun show(){
        dialog.show()
    }

}