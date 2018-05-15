package io.forus.me

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.forus.me.entities.Token
import io.forus.me.helpers.ThreadHelper
import io.forus.me.helpers.TransferViewModel
import io.forus.me.services.Web3Service
import io.forus.me.web3.TokenContract

class SendTokenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.loading)
        val transfer = TransferViewModel.fromJson(
                intent.getStringExtra(SendWalletItemActivity.RequestCode.TRANSFER_OBJECT))
        val recipient = intent.getStringExtra(SendWalletItemActivity.RequestCode.RECIPIENT)
        if (recipient == null || recipient.isEmpty()) {
            setResult(SendWalletItemActivity.ResultCode.FAILURE_NO_RECIPIENT)
        } else if (transfer != null && transfer.item is Token) {
            var transactionResult: Boolean? = null
            ThreadHelper.dispense(ThreadHelper.TOKEN_THREAD).postTask(Runnable {
                transactionResult = if (transfer.item.isEther) {
                    Web3Service.sendEther(recipient, transfer.value!!.toLong())
                } else {
                    val contract = TokenContract(transfer.item.address)
                    contract.transfer(recipient, transfer.value!!.toLong())
                }
            })
            while (transactionResult == null) Thread.sleep(500)
            setResult(if (transactionResult!!) SendWalletItemActivity.ResultCode.SUCCESS_RESULT else SendWalletItemActivity.ResultCode.FAILURE_RESULT, intent)
        } else {
            setResult(SendWalletItemActivity.ResultCode.FAILURE_INVALID_JSON)
        }
        finish()
    }

}