package io.forus.me.views.wallet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import io.forus.me.R
import io.forus.me.RequestWalletItemActivity
import io.forus.me.WalletItemActivity
import io.forus.me.entities.base.WalletItem
import io.forus.me.helpers.TransferViewModel
import io.forus.me.views.base.TitledFragment

class WalletItemRequestFragment : TitledFragment() {

    lateinit var amountField: TextView
    lateinit var descriptionField: TextView
    var walletItem: WalletItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.wallet_item_send_fragment, container, false)
        this.amountField = view.findViewById(R.id.amountField)
        this.descriptionField = view.findViewById(R.id.descriptionField)
        val nextButton: Button = view.findViewById(R.id.scanButton)
        nextButton.setOnClickListener({
            onNextPressed()
        })
        return view
    }

    private fun onNextPressed() {
        if (this.walletItem != null) {
            val description = descriptionField.text.toString()
            val amount = amountField.text.toString().toFloat()
            val transfer = TransferViewModel(this.walletItem!!, description, amount)
            val json = transfer.toJson().toString()
            val intent = Intent(this.context, RequestWalletItemActivity::class.java)
            intent.putExtra("data", json)
            startActivityForResult(intent, WalletItemActivity.RETRIEVE_REQUEST)
        }
    }
}