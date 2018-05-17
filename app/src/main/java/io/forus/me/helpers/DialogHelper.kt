package io.forus.me.helpers

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import io.forus.me.R

interface DialogHelper {

    companion object {
        fun build(context: Context, title: String, description:String, onCloseListener: OnCloseListener): AlertDialog.Builder {
            val dialog = AlertDialog.Builder(context)
            dialog.setOnCancelListener(onCloseListener)
            dialog.setOnDismissListener(onCloseListener)
            return dialog
        }

        fun setOnCloseListener(dialog: AlertDialog,  listener:OnCloseListener): AlertDialog {
            dialog.setOnCancelListener(listener)
            dialog.setOnDismissListener(listener)
            return dialog
        }

        fun stylize(dialog: AlertDialog, titleRes: Int, descriptionRes: Int, listener: OnCloseListener): AlertDialog {
            val layout = LayoutInflater.from(dialog.context).inflate(R.layout.alert_generic, null)
            val toolbar: Toolbar = layout.findViewById(R.id.toolbar)
            val titleText: TextView = layout.findViewById(R.id.titleView)
            val descriptionText: TextView = layout.findViewById(R.id.descriptionView)
            val closeButton: Button = layout.findViewById(R.id.closeButton)
            toolbar.setTitle(titleRes)
            titleText.setText(titleRes)
            descriptionText.setText(descriptionRes)
            closeButton.setOnClickListener(listener)
            dialog.setView(layout)
            return dialog
        }
    }

    abstract class OnCloseListener(private val dialog: DialogInterface): DialogInterface.OnDismissListener, DialogInterface.OnCancelListener, View.OnClickListener {
        abstract fun onClose(dialog: DialogInterface?)

        override fun onDismiss(dialog: DialogInterface?) {
            this.onClose(dialog)
        }

        override fun onCancel(dialog: DialogInterface?) {
            this.onClose(dialog)
        }

        override fun onClick(v: View?) {
            onClose(dialog)
        }

    }
}