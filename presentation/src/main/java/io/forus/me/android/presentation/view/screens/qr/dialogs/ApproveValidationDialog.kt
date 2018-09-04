package io.forus.me.android.presentation.view.screens.qr.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.presentation.R


class ApproveValidationDialog(private val context: Context,
                              private val validation: Validation,
                              private val positiveCallback: () -> Unit,
                              private val negativeCallback: () -> Unit,
                              private val cancelListener: () -> Unit){

    private val builder: MaterialDialog.Builder

    init {
        builder = MaterialDialog.Builder(context)
                .title(context.resources.getString(R.string.qr_popup_approve_validation_title))
                .content("\"" + validation.name+"\": \"" + validation.value + "\"")
                .positiveText(context.resources.getString(R.string.qr_popup_approve_validation_positive))
                .negativeText(context.resources.getString(R.string.qr_popup_approve_validation_negative))
                .onPositive { dialog, which -> positiveCallback.invoke() }
                .onNegative { dialog, which -> negativeCallback.invoke() }
                .cancelListener { cancelListener.invoke() }
    }

    fun show(){
        val dialog = builder.build()
        dialog.show()
    }
}