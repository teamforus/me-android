package io.forus.me.android.presentation.view.screens.qr.dialogs

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.ImageView
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class ScanVoucherBaseErrorDialog(
    private val message: String,
    private val context: Context,
    private val dismissListener: () -> Unit
) {

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
        .title(R.string.qr_popup_error)
        .content(message)
        //.icon(context.resources.getDrawable(R.drawable.ic_close))
        //.positiveText(context.resources.getString(R.string.me_ok))
        .dismissListener { dismissListener.invoke() }
        .negativeText(R.string.close)
        .cancelListener {  dismissListener.invoke()  }
        .build()

    init {
        Log.d("DialogScan", "ScanVoucherBaseErrorDialog init")

        val closeIcon = dialog.findViewById(R.id.icon)

        closeIcon?.apply {
            // Дозволяємо клікати на іконку
            isClickable = true

            // Якщо SDK версія >= Oreo (API 26), дозволяємо фокусування
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                isFocusable = true
            }

            // Додаємо обробник кліку
            setOnClickListener {
                Log.d("DialogScan", "ScanVoucherBaseErrorDialog setOnClickListener")
                dismissListener.invoke() // Викликаємо дію при натисканні
            }

            // Застосовуємо тінт колір
          //  setColorFilter(context.getColor(R.color.my_tint_color), PorterDuff.Mode.SRC_IN)
        }
    }

    fun show() {
        Log.d("DialogScan", "ScanVoucherBaseErrorDialog") // не спрацьовує
        dialog.show()
    }
}
