package io.forus.me.android.presentation.api_config.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class CustomApiDialog(private val context: Context, inputCallback: MaterialDialog.InputCallback,
                      private val saveListener: (MaterialDialog) -> Unit,
                      private val cancelListener: () -> Unit){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title("Custom api")
            .positiveText("Save")
            .input("","https://api.forus.io/",inputCallback)
            .negativeText("Cancel")
            .onPositive { dialog, which ->  saveListener.invoke(dialog)  }
            .cancelListener { cancelListener.invoke() }
            .build()

    fun show(){
        dialog.show()
    }
}