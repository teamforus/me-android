package io.forus.me.android.presentation.view.screens.qr.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class RestoreIdentityDialog(private val context: Context,
                            private val positiveCallback: () -> Unit,
                            private val cancelListener: () -> Unit){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(context.resources.getString(R.string.qr_popup_restore_identity_title))
            .content(context.resources.getString(R.string.qr_popup_restore_identity_description))
            .positiveText(context.resources.getString(R.string.qr_popup_restore_identity_positive))
            .onPositive { dialog, which -> positiveCallback.invoke() }
            .cancelListener { cancelListener.invoke() }
            .build()

    fun show(){
        dialog.show()
    }
}