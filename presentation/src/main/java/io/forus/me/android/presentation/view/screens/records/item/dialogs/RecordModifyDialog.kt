package io.forus.me.android.presentation.view.screens.records.item.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import io.forus.me.android.presentation.R


class RecordModifyDialog(private val context: Activity, val action: Action,  private val edit: () -> Unit) {

    private var dialog: AlertDialog

    init {

        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView = inflater.inflate(R.layout.view_edit_record_dialog, null)

        val cancel_bt = dialogView.findViewById<TextView>(R.id.cancel_bt)
        cancel_bt.setOnClickListener { dismiss() }
        val ok_bt = dialogView.findViewById<TextView>(R.id.ok_bt)
        ok_bt.setOnClickListener{
            edit.invoke()
            dismiss()
        }

        ok_bt.text = when (action) {
            Action.EDIT -> context.getString(R.string.dialog_button_edit)
            Action.DELETE -> context.getString(R.string.dialog_button_archive)
        }

        val description  = dialogView.findViewById<TextView>(R.id.description)
        description.text  = when (action) {
            Action.EDIT -> context.getString(R.string.dialog_record_edit_description)
            Action.DELETE -> context.getString(R.string.dialog_record_archive_description)
        }

        dialogBuilder.setView(dialogView)
        dialog = dialogBuilder.create()
        if (dialog.window != null) {
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }



    public enum class Action {
        EDIT, DELETE
    }

}