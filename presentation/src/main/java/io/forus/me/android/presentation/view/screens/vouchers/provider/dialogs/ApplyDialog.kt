package io.forus.me.android.presentation.view.screens.vouchers.provider.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R
import java.math.BigDecimal

class ApplyDialog(private val context: Context,
                  private val positiveCallback: () -> Unit) {

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(context.resources.getString(R.string.vouchers_pay_title))
            .content(context.resources.getString(R.string.vouchers_apply_question))
            .positiveText(context.resources.getString(R.string.voucher_request))
            .negativeText(context.resources.getString(R.string.me_cancel))
            .onPositive { dialog, which -> positiveCallback.invoke() }
            .build()

    fun show(){
        dialog.show()
    }
}