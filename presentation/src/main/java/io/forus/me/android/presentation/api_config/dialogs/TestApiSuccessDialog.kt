package io.forus.me.android.presentation.api_config.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog

class TestApiSuccessDialog(
    context: Context,
    message: String,
    private val positiveCallback: () -> Unit
) {
    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
        .title("Success test API server")
        .content(message)
        .positiveText("OK")
        .onPositive { dialog, which -> positiveCallback.invoke() }
        .build()

    fun show() {
        dialog.show()
    }
}