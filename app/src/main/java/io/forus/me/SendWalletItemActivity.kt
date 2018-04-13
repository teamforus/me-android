package io.forus.me

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView
import io.forus.me.entities.base.EthereumItem
import io.forus.me.helpers.QrHelper
import io.forus.me.helpers.ThreadHelper
import io.forus.me.helpers.TransferViewModel
import io.forus.me.services.Web3Service
import io.forus.me.views.me.MeFragment
import io.forus.me.web3.TokenContract

import kotlinx.android.synthetic.main.activity_send_wallet_item.*

class SendWalletItemActivity : AppCompatActivity() {

    private lateinit var scanner: BarcodeView

    fun cancel(view:View) {
        this.setResult(WalletItemActivity.CANCEL_RESULT)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_wallet_item)
        setSupportActionBar(toolbar)

        val json = intent.extras.getString("data")
        val transfer = TransferViewModel.fromJson(json)
        if (transfer != null) {
            val nameView:TextView = findViewById(R.id.valueText)
            var name = ""
            if (transfer.value != null) name += transfer.value.toString() + " "
            name += transfer.item.name
            nameView.text = name
            val descriptionView:TextView = findViewById(R.id.descriptionText)
            descriptionView.text = transfer.description
            this.scanner = findViewById(R.id.qrScanner)

            requestPermission()
            scanner.decodeContinuous(object : BarcodeCallback {
                override fun barcodeResult(result: BarcodeResult) {
                    pauseScanner()
                    var transactionResult: Boolean? = null
                    setContentView(R.layout.loading)
                    ThreadHelper.dispense(ThreadHelper.TOKEN_THREAD).postTask(Runnable {
                        val contract = TokenContract(transfer.item.address)
                        transactionResult = contract.transfer(result.text, transfer.value!!.toLong())
                    })
                    if (!transactionResult!!) {
                        Toast.makeText(baseContext, "Error!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(baseContext, "Transactie gelukt. Het kan even duren voordat dit zichtbaar is", Toast.LENGTH_SHORT).show()
                    }
                    val intent = Intent()
                    intent.putExtra(WalletItemActivity.WALLET_ITEM_KEY, json)
                    intent.putExtra(WalletItemActivity.ADDRESS_KEY, result.text)
                    setResult(WalletItemActivity.OK_RESULT, intent)
                    // TODO send to API?
                    finish()
                }

                override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
                    //setMarkers(resultPoints)
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        scanner.pause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isEmpty()) {
            requestPermission()
        } else {
            scanner.resume()
        }
    }

    override fun onResume() {
        super.onResume()
        scanner.resume()
    }

    fun pauseScanner() {
        scanner.pause()
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        }
    }

    fun resumeScanner() {
        scanner.resume()
    }

    fun setMarkers(resultPoints: List<ResultPoint>) {
        val view:RelativeLayout? = findViewById(R.id.markerContainer)
        if (view != null) {
            view.removeAllViews()
            for (resultPoint in resultPoints) {
                val marker = ImageView(baseContext)
                marker.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.red))
                val params = RelativeLayout.LayoutParams(5, 5)
                params.leftMargin = resultPoint.x.toInt()
                params.topMargin = resultPoint.y.toInt()
                view.addView(marker, params)
            }
        }
    }

}
