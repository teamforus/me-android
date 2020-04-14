package io.forus.me.android.presentation.view.screens.qr.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class ScanDemoTransactionDialog(private val context: Context,
                                private val positiveCallback: () -> Unit){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title("Success") //translate will be available after merge issue #377 //context.resources.getString(R.string.qr_popup_restore_identity_title))
            .content(context.resources.getString(R.string.voucher_demo_transaction_operation_completed))
            .positiveText(context.resources.getString(R.string.me_ok))
            .onPositive { dialog, which -> positiveCallback.invoke() }
            .cancelListener { dismiss() }
            .build()

    fun show(){
        dialog.show()
    }

    fun dismiss(){
        dialog.dismiss()
    }
}