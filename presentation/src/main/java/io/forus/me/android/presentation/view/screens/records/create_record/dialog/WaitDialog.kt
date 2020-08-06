package io.forus.me.android.presentation.view.screens.records.create_record.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.screens.records.item.dialogs.RecordModifyDialog
import kotlinx.android.synthetic.main.fragment_account_details.*

class WaitDialog(private val context: Activity){

    var  dialog: AlertDialog



    init {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView = inflater.inflate(R.layout.view_wait_dialog, null)

        dialogBuilder.setView(dialogView)
        dialog = dialogBuilder.create()
        if (dialog.window != null) {
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun show(){
        dialog.show()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window.attributes = layoutParams
        }
    }

    fun dismiss(){
        if(dialog.isShowing){
            dialog.dismiss()
        }

    }
}