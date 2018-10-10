package io.forus.me.android.presentation.view.screens.vouchers.provider

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R

class PayDialog(private val context: Context,
                              private val amount: Float,
                              private val positiveCallback: () -> Unit) {

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(context.resources.getString(R.string.vouchers_pay_title))
            .content(context.resources.getString(R.string.vouchers_pay_question, amount.toString()))
            .positiveText(context.resources.getString(R.string.me_ok))
            .negativeText(context.resources.getString(R.string.me_cancel))
            .onPositive { dialog, which -> positiveCallback.invoke() }
            .build()

    fun show(){
        dialog.show()
    }
}