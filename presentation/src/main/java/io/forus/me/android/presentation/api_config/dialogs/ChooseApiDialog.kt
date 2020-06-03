package io.forus.me.android.presentation.api_config.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class ChooseApiDialog(private val context: Context, itemCallback: MaterialDialog.ListCallback,
                      private val cancelListener: () -> Unit){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title("Choose api")
            .items(R.array.api_items)
            .negativeText("Cancel")
            .itemsCallback(itemCallback)
            .cancelListener { cancelListener.invoke() }
            .build()

    fun show(){
        dialog.show()
    }
}