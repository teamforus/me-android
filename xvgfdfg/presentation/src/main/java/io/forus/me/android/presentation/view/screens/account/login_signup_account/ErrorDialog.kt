package io.forus.me.android.presentation.view.screens.account.login_signup_account

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R

class ErrorDialog(private val context: Context, private val title: String, private val message: String){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(title)
            .customView(R.layout.view_about_me, false)
            .positiveText(context.resources.getString(R.string.me_ok))
            //.onPositive(callback)
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