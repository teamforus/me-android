package io.forus.me.android.presentation.view.screens.account.assigndelegates.pin

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R

class PinInstructionsDialog(private val context: Context){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(R.string.restore_title_pincode)
            .content(R.string.restore_pincode_instructions)
            .positiveText(context.resources.getString(R.string.me_ok))
            .build()

    fun show(){
        dialog.show()
    }
}