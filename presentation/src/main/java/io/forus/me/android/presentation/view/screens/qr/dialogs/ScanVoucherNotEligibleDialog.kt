package io.forus.me.android.presentation.view.screens.qr.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import io.forus.me.android.presentation.R


class ScanVoucherNotEligibleDialog(private val context: Context,
                                   private val dismissListener: () -> Unit){

   /* private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(R.string.qr_popup_error)
            .content(R.string.qr_voucher_access_denied)
            .icon(context.resources.getDrawable(R.drawable.ic_close))
            .positiveText(context.resources.getString(R.string.me_ok))
            .dismissListener { dismissListener.invoke() }
            .build()*/

    fun show(){
       // dialog.show()
        MaterialDialog(context)
                .title(R.string.qr_popup_error)
                .message(R.string.qr_voucher_access_denied)
                .icon(R.drawable.ic_close)
                .show {
                    positiveButton(R.string.me_ok)
                    onCancel { dismissListener.invoke() }
                }
                .setContentView(R.layout.view_approve_validation)
    }
}