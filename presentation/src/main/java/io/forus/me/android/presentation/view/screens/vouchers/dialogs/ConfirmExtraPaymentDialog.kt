package io.forus.me.android.presentation.view.screens.vouchers.dialogs

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format


class ConfirmExtraPaymentDialog(
    val extraAmount: Float,
    var onConfirm: (Float) -> Unit,
    var onCancel: () -> Unit
) : BottomSheetDialogFragment() {

    var btClose: View? = null
    var moreInfoButtonContainer: View? = null
    var extraInfoTextView: View? = null
    var icMoreInfo: ImageView? = null
    var extraAmountTV: TextView? = null
    var alertTitleTV : TextView? = null
    var textInputLayout: com.google.android.material.textfield.TextInputLayout? = null
    var extraAmountInput: com.google.android.material.textfield.TextInputEditText? = null
    var cancel: View? = null
    var submit: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.dialog_apply_action_extra_amount, container, false)

        btClose = root.findViewById(R.id.btClose)
        moreInfoButtonContainer = root.findViewById(R.id.moreInfoButtonContainer)
        extraInfoTextView = root.findViewById(R.id.extraInfoText)
        extraAmountInput = root.findViewById(R.id.extraAmountInput)
        icMoreInfo = root.findViewById(R.id.icMoreInfo)
        textInputLayout = root.findViewById(R.id.name_text_input)
        extraAmountTV = root.findViewById(R.id.extraAmount)
        cancel = root.findViewById(R.id.cancel)
        submit = root.findViewById(R.id.submit)
        alertTitleTV = root.findViewById(R.id.alertTitle)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetDialog = dialog as? BottomSheetDialog
        val bottomSheetView = bottomSheetDialog?.findViewById<View>(R.id.design_bottom_sheet)
        bottomSheetView?.let {
            val behavior = BottomSheetBehavior.from(it)

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isFitToContents = true
        }

        extraAmountInput?.hint = "€ 0,00"
        alertTitleTV?.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
        extraAmountInput?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                alertTitleTV?.setTextColor(ContextCompat.getColor(requireContext(), R.color.forus_blue))
                extraAmountInput?.hint = ""
            } else {
                if (extraAmountInput?.text.isNullOrEmpty()) {
                    alertTitleTV?.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
                    extraAmountInput?.hint = "€ 0,00"
                }
            }
        }


        btClose?.setOnClickListener {
            onCancel()
            dismiss()
        }
        cancel?.setOnClickListener {
            onCancel()
            dismiss()
        }
        submit?.setOnClickListener {
            try {
                val inputExtraAmount = extraAmountInput?.text.toString().toFloatOrNull() ?: 0f

                if (inputExtraAmount != extraAmount) {
                    textInputLayout?.error = getString(R.string.check_extra_amount)
                } else {
                    textInputLayout?.error = null
                    onConfirm(extraAmount)
                    dismiss()
                }
            } catch (e: Exception) {
                textInputLayout?.error = getString(R.string.check_extra_amount)
            }

        }

        extraAmountTV?.text = "€ ${extraAmount.format(2)}"

        moreInfoButtonContainer?.setOnClickListener {
            if (extraInfoTextView?.visibility == View.VISIBLE) {
                extraInfoTextView?.visibility = View.GONE

                icMoreInfo?.rotation = 0f
            } else {
                extraInfoTextView?.visibility = View.VISIBLE
                icMoreInfo?.rotation = 90f
            }

        }


    }

    /* override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
         return BottomSheetDialog(requireContext(), R.style.RoundedBottomSheetDialog)
     }*/

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }
}