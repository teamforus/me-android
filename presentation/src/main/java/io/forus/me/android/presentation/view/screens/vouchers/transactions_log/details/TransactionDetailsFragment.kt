package io.forus.me.android.presentation.view.screens.vouchers.transactions_log.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentPopupTransactionDetailsBinding


class TransactionDetailsFragment : BottomSheetDialogFragment() {


    var id: String = ""
    var fund: String = ""
    var provider: String = ""
    var amount: String = ""
    var status: String = ""
    var formattedDate : String = ""

    companion object {
        private val TRANSACTION_DATE = "TRANSACTION_DATE"
        private val TRANSACTION_ID = "TRANSACTION_ID"
        private val TRANSACTION_FUND = "TRANSACTION_FUND"
        private val TRANSACTION_PROVIDER = "TRANSACTION_PROVIDER"
        private val TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT"
        private val TRANSACTION_STATUS = "TRANSACTION_STATUS"

        fun newInstance(
            id: String,
            fund: String?,
            provider: String?,
            amount: String,
            status: String?,
            formattedDate: String,
        ): TransactionDetailsFragment = TransactionDetailsFragment().also {
            val bundle = Bundle()
            bundle.putString(TRANSACTION_DATE, formattedDate)
            bundle.putString(TRANSACTION_ID, id)
            if (fund != null) bundle.putString(TRANSACTION_FUND, fund)
            if (provider != null) bundle.putString(TRANSACTION_PROVIDER, provider)
            bundle.putString(TRANSACTION_AMOUNT, amount)
            if (status != null) bundle.putString(TRANSACTION_STATUS, status)
            it.arguments = bundle
        }
    }




    private lateinit var binding: FragmentPopupTransactionDetailsBinding

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogThemeGray
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPopupTransactionDetailsBinding.inflate(inflater)

        val bundle = this.arguments
        if (bundle != null) {

            id = bundle.getString(TRANSACTION_ID, "")
            fund = bundle.getString(TRANSACTION_FUND, "")
            provider = bundle.getString(TRANSACTION_PROVIDER, "")
            amount = bundle.getString(TRANSACTION_AMOUNT, "")
            status = bundle.getString(TRANSACTION_STATUS, "")
            formattedDate = bundle.getString(TRANSACTION_DATE, "")

        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.amountTV.text = amount
        binding.statusTV.text = status
        binding.idTV.text = id
        binding.fundTV.text = fund
        binding.providerTV.text = provider
        binding.dateTextLb.text = formattedDate

        binding.btClose.setOnClickListener {
            dismiss()
        }
    }



}

