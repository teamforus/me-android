package io.forus.me.android.presentation.view.screens.qr.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class ScanVoucherBaseErrorDialog(private val message: String, private val context: Context,
                                 private val dismissListener: () -> Unit){

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