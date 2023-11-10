package io.forus.me.android.presentation.view.screens.account.account.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.component.text.TextView


class LogoutDialog(private val context: Context,
                   private val positiveCallback: () -> Unit){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(context.resources.getString(R.string.profile_logout_dialog_title))
            .content(R.string.profile_logout_dialog_content)
            .positiveText(context.resources.getString(R.string.profile_logout))
            .negativeText(context.resources.getString(R.string.cancel))
            .onPositive { dialog, which -> positiveCallback.invoke() }
            .onNegative{ dialog, which -> dismiss() }
            .cancelListener { dismiss() }
            .build()

    init {
        val view = dialog.customView

    }

    fun show(){
        dialog.show()
    }

    fun dismiss(){
        dialog.dismiss()
    }

}