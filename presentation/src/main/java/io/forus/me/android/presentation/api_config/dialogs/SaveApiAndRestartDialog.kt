package io.forus.me.android.presentation.api_config.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R
import java.math.BigDecimal
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.view.screens.main.MainActivity


class SaveApiAndRestartDialog(private val context: Context,
                              private val positiveCallback: () -> Unit) {

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title("Confirm")
            .content("You must restart the application for the changes to take effect")
            .positiveText("Save and restart")
            .negativeText(context.resources.getString(R.string.me_cancel))
            .onPositive { dialog, which ->
                positiveCallback.invoke()

            }
            .build()

    fun show() {
        dialog.show()
    }


}