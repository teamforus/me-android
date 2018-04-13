package io.forus.me

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import io.forus.me.helpers.QrHelper
import io.forus.me.helpers.TransferViewModel

import kotlinx.android.synthetic.main.activity_request_wallet_item.*

class RequestWalletItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_wallet_item)
        setSupportActionBar(toolbar)

        val json = intent.extras.getString("data")
        val transfer = TransferViewModel.fromJson(json)
        if (transfer != null) {
            val nameView: TextView = findViewById(R.id.valueText)
            var name = ""
            if (transfer.value != null) name += transfer.value.toString() + " "
            name += transfer.item.name
            nameView.text = name
            val descriptionView: TextView = findViewById(R.id.descriptionText)
            descriptionView.text = transfer.description
            val qrView: ImageView = findViewById(R.id.qrView)
            qrView.setImageBitmap(QrHelper.getQrBitmap(json,
                    500,
                    ContextCompat.getColor(baseContext, R.color.black),
                    ContextCompat.getColor(baseContext, R.color.white)))

            // TODO listen for the qr code being scanned by another device (API?)
        }
    }

}
