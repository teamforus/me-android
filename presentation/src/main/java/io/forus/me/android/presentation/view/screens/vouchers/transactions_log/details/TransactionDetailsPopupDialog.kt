package io.forus.me.android.presentation.view.screens.vouchers.transactions_log.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.extensions.formatToDisplay
import java.text.NumberFormat
import java.util.Locale


class TransactionDetailsPopupDialog(
    val transaction: Transaction
) : BottomSheetDialogFragment() {

    private lateinit var closeIV: ImageView
    private lateinit var dateTimeTV: TextView
    private lateinit var amountTV: TextView
    private lateinit var statusTV: TextView
    private lateinit var idTV: TextView
    private lateinit var fundTV: TextView
    private lateinit var providerTV: TextView
    private lateinit var extraAmountTV: TextView
    private lateinit var noteTV: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_popup_transaction_details, container, false)

        closeIV = root.findViewById(R.id.btClose)
        dateTimeTV = root.findViewById(R.id.dateTimeTV)
        amountTV = root.findViewById(R.id.amountTV)
        statusTV = root.findViewById(R.id.statusTV)
        idTV = root.findViewById(R.id.idTV)
        fundTV = root.findViewById(R.id.fundTV)
        providerTV = root.findViewById(R.id.providerTV)
        extraAmountTV = root.findViewById(R.id.extraAmoutValue)
        noteTV = root.findViewById(R.id.noteValue)


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


        closeIV.setOnClickListener {
            try {
                dismiss()
            } catch (e: IllegalStateException) {
                Log.e("TransactionDetailsPopupDialog", "Error while dismissing TransactionDetailsPopupDialog: ${e.message}")
            }
        }

        dateTimeTV.text = transaction.createdAt.formatToDisplay()
        amountTV.text = NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
            .format(transaction.amount)

        val status =
            when (transaction.state) {
                "success" -> getString(R.string.transaction_paid)
                "pending" -> getString(R.string.status_pending)
                else -> transaction.state ?: ""
            }

        statusTV.text = status
        idTV.text = transaction.id
        fundTV.text = transaction.fund?.name
        providerTV.text = transaction.organization?.name
        extraAmountTV.text = NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
            .format(transaction.amount_extra_cash)
        noteTV.text = transaction.note ?: ""


    }


    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }
}