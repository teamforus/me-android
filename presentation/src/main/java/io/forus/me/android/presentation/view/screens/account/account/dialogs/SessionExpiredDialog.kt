package io.forus.me.android.presentation.view.screens.account.account.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R
import kotlinx.android.synthetic.main.fragment_account_details.*

class SessionExpiredDialog(private val context: Context, private val dismissListener: () -> Unit){

    /*private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(context.getString(R.string.warning))
            .customView(R.layout.view_about_me, false)
            .positiveText(context.resources.getString(R.string.me_ok))
            .onPositive(callback)
            .build()



    init {
        val view = dialog.customView
        val message = view?.findViewById<io.forus.me.android.presentation.view.component.text.TextView>(R.id.message);
        val s = SpannableString(context.getText(R.string.session_has_expired));
        Linkify.addLinks(s, Linkify.WEB_URLS);
        message?.setText(s);
        message?.setMovementMethod(LinkMovementMethod.getInstance());
    }*/

    /*fun show(){
        dialog.show()
    }*/

    fun show() {
        //dialog.show()
        MaterialDialog(context)
                .title(R.string.warning)
                .message{
                    val s = SpannableString(context.getText(R.string.session_has_expired));
                    Linkify.addLinks(s, Linkify.WEB_URLS);
                }
                .show {
                    negativeButton(R.string.me_ok) { dialog ->
                        dismissListener.invoke()
                    }
                }
    }
}