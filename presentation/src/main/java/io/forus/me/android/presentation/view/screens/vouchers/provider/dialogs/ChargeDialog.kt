package io.forus.me.android.presentation.view.screens.vouchers.provider.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R
import java.math.BigDecimal

class ChargeDialog(private val context: Context,
                   private val chargeAmount: BigDecimal,
                   private val extra: BigDecimal,
                   private val positiveCallback: () -> Unit) {

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(context.resources.getString(R.string.vouchers_pay_title))
            .customView(if(extra.compareTo(BigDecimal.ZERO) == 0) R.layout.view_provider_charge else R.layout.view_provider_charge_extra, false)
            .positiveText(context.resources.getString(R.string.voucher_request))
            .negativeText(context.resources.getString(R.string.me_cancel))
            .onPositive { dialog, which -> positiveCallback.invoke() }
            .build()

    init {
        val view = dialog.customView
        val tvCharge = view?.findViewById<io.forus.me.android.presentation.view.component.text.TextView>(R.id.tv_charge)
        val tvExtra = view?.findViewById<io.forus.me.android.presentation.view.component.text.TextView>(R.id.tv_extra)

        tvCharge?.text = context.resources.getString(R.string.vouchers_pay_question, chargeAmount.toString())
        if(extra.compareTo(BigDecimal.ZERO) != 0){
            tvExtra?.text = context.resources.getString(R.string.vouchers_pay_question_extra, extra.toString())
        }
    }

    fun show(){
        dialog.show()
    }
}