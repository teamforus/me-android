package io.forus.me.android.presentation.api_config.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog

class TestApiErrorDialog(
    context: Context,
    message: String,
    private val cancelListener: () -> Unit
) {
    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
        .title("Error test API server")
        .content(message)
        .negativeText("Cancel")
        .cancelListener { cancelListener.invoke() }
        .build()

    fun show() {
        dialog.show()
    }
}