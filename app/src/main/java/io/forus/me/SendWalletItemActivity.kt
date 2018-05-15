package io.forus.me

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
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
import io.forus.me.entities.Asset
import io.forus.me.entities.Token
import io.forus.me.entities.base.EthereumItem
import io.forus.me.helpers.QrHelper
import io.forus.me.helpers.ThreadHelper
import io.forus.me.helpers.TransferViewModel
import io.forus.me.services.Web3Service
import io.forus.me.views.me.MeFragment
import io.forus.me.web3.TokenContract

import kotlinx.android.synthetic.main.activity_send_wallet_item.*

class SendWalletItemActivity : AppCompatActivity(), BarcodeCallback {

    private lateinit var scanner: BarcodeView
    private lateinit var viewModel: TransferViewModel

    override fun barcodeResult(result: BarcodeResult) {
        pauseScanner()
        val intent = Intent(this,
                when {
                    viewModel.item is Asset -> SendTokenActivity::class.java
                    viewModel.item is Token -> SendTokenActivity::class.java
                    else -> SendTokenActivity::class.java
                }
        )
        intent.putExtra(RequestCode.RECIPIENT, result.text)
        intent.putExtra(RequestCode.TRANSFER_OBJECT, viewModel.toJson().toString())
        startActivityForResult(intent, RequestCode.EXECUTE_SEND)
    }

    fun cancel(view:View) {
        this.setResult(ResultCode.CANCEL_RESULT)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ResultCode.FAILURE_NO_RECIPIENT || resultCode == ResultCode.FAILURE_INVALID_JSON || resultCode == ResultCode.FAILURE_RESULT) {
            Toast.makeText(this, "// TODO error ", Toast.LENGTH_LONG).show()
            resumeScanner()
        } else {
            setResult(resultCode, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_wallet_item)
        setSupportActionBar(toolbar)

        val json = intent.extras.getString(RequestCode.TRANSFER_OBJECT)
        val transfer = TransferViewModel.fromJson(json)
        if (transfer != null) {
            this.viewModel = transfer
            val nameView:TextView = findViewById(R.id.valueText)
            var name = ""
            if (transfer.value != null) name += transfer.value.toString() + " "
            name += transfer.item.name
            nameView.text = name
            val descriptionView:TextView = findViewById(R.id.descriptionText)
            descriptionView.text = transfer.description
            this.scanner = findViewById(R.id.qrScanner)
            requestPermission()
            scanner.decodeContinuous(this)
        } else {
            setResult(ResultCode.FAILURE_INVALID_JSON)
            finish()
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

    override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
        //setMarkers(resultPoints)
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

    class RequestCode {
        companion object {
            const val SEND_REQUEST = 801
            const val EXECUTE_SEND = 802

            const val RECIPIENT = "to"
            const val TRANSFER_OBJECT = "data"
        }
    }

    class ResultCode {
        companion object {
            const val FAILURE_INVALID_JSON = 811
            const val FAILURE_NO_RECIPIENT = 812
            const val FAILURE_RESULT = 813
            const val SUCCESS_RESULT = 814
            const val CANCEL_RESULT = 815

            const val FAILURE_MESSAGE = "message"
        }
    }
}
