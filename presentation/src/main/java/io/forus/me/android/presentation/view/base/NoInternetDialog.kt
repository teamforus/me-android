package io.forus.me.android.presentation.view.base

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class NoInternetDialog(private val context: Context,
                       private val dismissListener: () -> Unit){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(R.string.qr_popup_error)
            .content("No internet connection!")
           // .icon(context.resources.getDrawable(R.drawable.ic_close))
            .positiveText(context.resources.getString(R.string.me_ok))
            .dismissListener { dismissListener.invoke() }
            .contentColor(context.resources.getColor(R.color.error))
            .build()

    fun show(){
        dialog.show()
    }
}