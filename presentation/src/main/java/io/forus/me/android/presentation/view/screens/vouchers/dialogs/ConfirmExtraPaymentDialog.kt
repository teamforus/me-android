package io.forus.me.android.presentation.view.screens.vouchers.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format


class ConfirmExtraPaymentDialog(val extraAmount: Float,var onConfirm: (Float) -> Unit, var onCancel: () -> Unit) : BottomSheetDialogFragment() {

    var btClose: View? = null
    var moreInfoButtonContainer: View? = null
    var extraInfoTextView: View? = null
    var icMoreInfo: ImageView? = null
    var extraAmountTV: TextView? = null
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
        icMoreInfo = root.findViewById(R.id.icMoreInfo)
        extraAmountTV = root.findViewById(R.id.extraAmount)
        cancel = root.findViewById(R.id.cancel)
        submit = root.findViewById(R.id.submit)
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

        //


        btClose?.setOnClickListener {
            onCancel()
            dismiss()
        }
        cancel?.setOnClickListener {
            onCancel()
            dismiss()
        }
        submit?.setOnClickListener {
            val extraAmount = extraAmountTV?.text.toString().toFloatOrNull() ?: 0f
            onConfirm(extraAmount)
            dismiss()
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