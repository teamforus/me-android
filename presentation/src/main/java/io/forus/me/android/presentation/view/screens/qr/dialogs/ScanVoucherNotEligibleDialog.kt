package io.forus.me.android.presentation.view.screens.qr.dialogs

import android.content.Context
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class ScanVoucherNotEligibleDialog(
    private val context: Context,
    private val dismissListener: () -> Unit
) {

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
        .title(R.string.qr_popup_error)
        .content(R.string.qr_voucher_access_denied)
        //.icon(context.resources.getDrawable(R.drawable.ic_close))
        //.positiveText(context.resources.getString(R.string.me_ok))
        .dismissListener { dismissListener.invoke() }
        .negativeText(R.string.close)
        .cancelListener { dismissListener.invoke() }
        .build()

    fun show() {
        Log.d("DialogScan", "ScanVoucherNotEligibleDialog")
        dialog.show()
    }
}