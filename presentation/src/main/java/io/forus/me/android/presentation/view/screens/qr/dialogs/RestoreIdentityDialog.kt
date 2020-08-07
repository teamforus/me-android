package io.forus.me.android.presentation.view.screens.qr.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.customview.customView
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.component.text.TextView


class RestoreIdentityDialog(private val context: Context,
                            private val positiveCallback: () -> Unit,
                            private val cancelListener: () -> Unit){

    /*private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(context.resources.getString(R.string.qr_popup_restore_identity_title))
            .content(context.resources.getString(R.string.qr_popup_restore_identity_description))
            .positiveText(context.resources.getString(R.string.me_ok))
            .onPositive { dialog, which -> positiveCallback.invoke() }
            .cancelListener { cancelListener.invoke() }
            .build()*/

    fun show(){
        //dialog.show()
        MaterialDialog(context)
                .title(R.string.qr_popup_restore_identity_title)
                .message(R.string.qr_popup_restore_identity_description)
                .show {
                    positiveButton(R.string.me_ok) { dialog ->
                        positiveCallback.invoke()
                    }
                    onCancel { cancelListener.invoke() }
                }
                .setContentView(R.layout.view_approve_validation)
    }
}