package io.forus.me.android.presentation.api_config.dialogs

import android.content.Context
import androidx.core.text.HtmlCompat
import android.text.Html
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class CustomApiDialog(private val context: Context, customApiStr: String,  inputCallback: MaterialDialog.InputCallback,
                      private val saveListener: (MaterialDialog) -> Unit,
                      private val cancelListener: () -> Unit){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title("Custom API server")
            .content(HtmlCompat.fromHtml("API format: <br/><b>https://{address}/</b> <i><font color='#DCDCDC'>api/v1/...</font></i>",
                    HtmlCompat.FROM_HTML_MODE_LEGACY))
            .positiveText("Save")
            .input("",customApiStr,inputCallback)
            .negativeText("Cancel")
            .onPositive { dialog, which ->  saveListener.invoke(dialog)  }
            .cancelListener { cancelListener.invoke() }
            .build()

    fun show(){
        dialog.show()
    }


}