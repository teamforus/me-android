package io.forus.me.android.presentation.view.screens.records.create_record.dialog

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class CreateRecordErrorDialog(private val context: Context,private val title: String,private val message: String, callback: MaterialDialog.SingleButtonCallback){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(title)
            .customView(R.layout.view_about_me, false)
            .positiveText(context.resources.getString(R.string.me_ok))
            .onPositive(callback)
            .build()



    init {
        val view = dialog.customView
        val messageTV = view?.findViewById<io.forus.me.android.presentation.view.component.text.TextView>(R.id.message);

        messageTV?.setText(message);
    }

    fun show(){
        dialog.show()
    }
}