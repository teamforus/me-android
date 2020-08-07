package io.forus.me.android.presentation.view.screens.records.item.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.customview.customView
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.component.text.TextView


class DeleteRecordErrorDialog(private val message: String, private val context: Context) {


    fun show() {
        MaterialDialog(context)
                .title(R.string.qr_popup_error)
                .message { message }
                .show {
                }
                .positiveButton(R.string.me_ok)
                .onCancel { }
    }


}