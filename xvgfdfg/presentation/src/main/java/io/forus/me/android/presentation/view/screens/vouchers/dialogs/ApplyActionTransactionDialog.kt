package io.forus.me.android.presentation.view.screens.vouchers.dialogs

import android.app.Activity
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class ApplyActionTransactionDialog(private val context: Activity, private val title: String,
                                   private val toPayText: String, private val subtitle: String,
                                   private val positiveCallback: () -> Unit) {

    var dialog: MaterialDialog? = null

    fun show() {

        val display: Display = context.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density: Float = context.resources.displayMetrics.density
        val dpHeight = outMetrics.heightPixels / density
        val dpWidth = outMetrics.widthPixels / density

        val viewId = if (dpWidth <= 320 || dpHeight < 522) {
            R.layout.dialog_apply_action_nokia1
        }else{
            R.layout.dialog_apply_action
        }

        val customLayout: View = context.layoutInflater.inflate(viewId, null)
        val titleTV = customLayout.findViewById<TextView>(R.id.title)
        val bottomTV = customLayout.findViewById<TextView>(R.id.bottom)

        val amountTV =  customLayout.findViewById<TextView>(R.id.amount)




            amountTV.text = toPayText
            titleTV.text = title
            bottomTV.text =  subtitle



        customLayout.findViewById<View>(R.id.submit).setOnClickListener {
            positiveCallback.invoke()
            dismiss()
        }
        customLayout.findViewById<View>(R.id.cancel).setOnClickListener {
            dismiss()
        }


        dialog = MaterialDialog.Builder(context)
                .customView(customLayout, false)
                //.positiveText(context.resources.getString(R.string.submit))
                //.negativeText(context.resources.getString(R.string.me_cancel))
                //.onPositive { dialog, which -> positiveCallback.invoke() }
                .build()
        dialog!!.show()

    }

    fun dismiss() {
        dialog!!.dismiss()
    }


}