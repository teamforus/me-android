package io.forus.me.android.presentation.view.screens.vouchers.transactions_log.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_popup_qr.*
import kotlinx.android.synthetic.main.fragment_popup_transaction_details.*


class TransactionDetailsFragment : BaseFragment() {



    var id: String = ""
    var fund : String = ""
    var provider : String = ""
    var amount : String = ""
    var status : String = ""

    companion object {
        private val TRANSACTION_ID = "TRANSACTION_ID"
        private val TRANSACTION_FUND = "TRANSACTION_FUND"
        private val TRANSACTION_PROVIDER = "TRANSACTION_PROVIDER"
        private val TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT"
        private val TRANSACTION_STATUS = "TRANSACTION_STATUS"

        fun newIntent(id: String, fund: String?, provider: String?, amount: String, status: String?): TransactionDetailsFragment = TransactionDetailsFragment().also {
            val bundle = Bundle()
            bundle.putString(TRANSACTION_ID, id)
            if(fund != null) bundle.putString(TRANSACTION_FUND, fund)
            if(provider != null) bundle.putString(TRANSACTION_PROVIDER, provider)
            bundle.putString(TRANSACTION_AMOUNT, amount)
            if(status != null) bundle.putString(TRANSACTION_STATUS, status)
            it.arguments = bundle
        }
    }
    override fun getLayoutID(): Int {
        return R.layout.fragment_popup_transaction_details
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       initUI()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState).also{
            val bundle = this.arguments
            if (bundle != null) {
                id = bundle.getString(TRANSACTION_ID, "")
                fund = bundle.getString(TRANSACTION_FUND, "")
                provider = bundle.getString(TRANSACTION_PROVIDER, "")
                amount = bundle.getString(TRANSACTION_AMOUNT, "")
                status = bundle.getString(TRANSACTION_STATUS, "")
            }
        }
    }

    override fun initUI() {
        amountTV.text = amount
        statusTV.text = status
        idTV.text = id
        fundTV.text = fund
        providerTV.text = provider


    }
}

