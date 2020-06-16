package io.forus.me.android.presentation.view.screens.records.item.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class DeleteRecordErrorDialog(private val message: String, private val context: Context){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(R.string.qr_popup_error)
            .content(message)
            .positiveText(context.resources.getString(R.string.me_ok))
            .dismissListener { dismiss()}
            .build()

    fun show(){
        dialog.show()
    }

    fun dismiss(){
        dialog.dismiss()
    }
}