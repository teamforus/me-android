package io.forus.me.android.presentation.view.screens.qr.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.customview.customView
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.component.text.TextView


class ApproveValidationDialog(private val context: Context,
                              private val validation: Validation,
                              private val positiveCallback: () -> Unit,
                              private val negativeCallback: () -> Unit,
                              private val cancelListener: () -> Unit){





            /*MaterialDialog.Builder(context)
            .title(context.resources.getString(R.string.qr_popup_approve_validation_title))
            .customView(R.layout.view_approve_validation, false)
            .positiveText(context.resources.getString(R.string.qr_popup_approve_validation_positive))
            .negativeText(context.resources.getString(R.string.qr_popup_approve_validation_negative))
            .onPositive { dialog, which -> positiveCallback.invoke() }
            .onNegative { dialog, which -> negativeCallback.invoke() }
            .cancelListener { cancelListener.invoke() }
            .build()*/

    init {
        /*val view = dialog.customView
        view?.findViewById<TextView>(R.id.type)?.text = validation.name
        view?.findViewById<TextView>(R.id.value)?.text = validation.value*/
    }

    fun show(){
       // dialog.show()

        MaterialDialog(context)
                .title(R.string.dialog_no_internet_warning)
                .show {
                    val view = this.customView()
                    view?.findViewById<TextView>(R.id.type)?.text = validation.name
                    view?.findViewById<TextView>(R.id.value)?.text = validation.value
                    positiveButton(R.string.qr_popup_approve_validation_positive) { dialog ->
                        positiveCallback.invoke()
                    }
                    negativeButton(R.string.qr_popup_approve_validation_negative) { dialog ->
                        negativeCallback.invoke()
                    }
                    onCancel { cancelListener.invoke() }
                }
                .setContentView(R.layout.view_approve_validation)
    }
}