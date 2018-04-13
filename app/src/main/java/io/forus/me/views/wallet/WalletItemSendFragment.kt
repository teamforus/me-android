package io.forus.me.views.wallet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import io.forus.me.R
import io.forus.me.SendWalletItemActivity
import io.forus.me.WalletItemActivity
import io.forus.me.entities.Service
import io.forus.me.entities.Token
import io.forus.me.entities.base.WalletItem
import io.forus.me.helpers.JsonHelper
import io.forus.me.helpers.TransferViewModel
import io.forus.me.views.base.TitledFragment

class WalletItemSendFragment : TitledFragment() {

    lateinit var amountField: TextView
    lateinit var descriptionField: TextView
    var walletItem:WalletItem? = null

    fun onCancel() {
        val canceledText: TextView = view!!.findViewById(R.id.canceledText)
        canceledText.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.wallet_item_send_fragment, container, false)
        this.amountField = view.findViewById(R.id.amountField)
        amountField.addTextChangedListener(MaxAmountWatcher())
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
            val intent = Intent(this.context, SendWalletItemActivity::class.java)
            intent.putExtra("data", json)
            startActivityForResult(intent, WalletItemActivity.SEND_REQUEST)
        }
    }

    inner class MaxAmountWatcher: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null && (walletItem is Token || walletItem is Service)) {
                if (walletItem is Token) {
                    val input = s.toString().toFloatOrNull()
                    if (input != null && input > (walletItem!! as Token).value) {
                        amountField.text = (walletItem!! as Token).value.toString()
                        Selection.setSelection(amountField.editableText,s.length)
                    }
                }
            }

        }

    }
}