package io.forus.me.android.presentation.view.screens.vouchers.dialogs

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class ApplyActionTransactionDialog(private val context: Activity, private val amount: String,
                                   private val positiveCallback: () -> Unit) {


    fun show() {

        val customLayout: View = context.layoutInflater.inflate(R.layout.dialog_apply_action, null)

        customLayout.findViewById<TextView>(R.id.title).text = context.getString(R.string.has_the_customer)
        customLayout.findViewById<TextView>(R.id.amount).text = amount
        customLayout.findViewById<TextView>(R.id.bottom).text = context.getString(R.string.paid_at_till)

        val dialog: MaterialDialog = MaterialDialog.Builder(context)
                .customView(customLayout, false)
                .positiveText(context.resources.getString(R.string.submit))
                .negativeText(context.resources.getString(R.string.me_cancel))
                .onPositive { dialog, which -> positiveCallback.invoke() }
                .build()



        dialog.show()

    }
}