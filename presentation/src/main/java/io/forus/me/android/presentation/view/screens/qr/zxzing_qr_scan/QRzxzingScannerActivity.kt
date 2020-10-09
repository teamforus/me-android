package io.forus.me.android.presentation.view.screens.qr.zxzing_qr_scan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import io.forus.me.android.presentation.R
import kotlinx.android.synthetic.main.activity_zxzing_capture.*


class QRzxzingScannerActivity : FragmentActivity() {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, QRzxzingScannerActivity::class.java)
        }
    }



    private var capture: CaptureManager? = null
    private var barcodeScannerView: DecoratedBarcodeView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zxzing_capture)




        barcodeScannerView = initializeContent()
        barcodeScannerView!!.setStatusText("")
        capture = CaptureManager(this, barcodeScannerView)
        capture!!.initializeFromIntent(intent, savedInstanceState)
        capture!!.decode()
    }

    /**
     * Override to use a different layout.
     *
     * @return the DecoratedBarcodeView
     */
    protected fun initializeContent(): DecoratedBarcodeView? {
        setContentView(R.layout.activity_zxzing_capture)
        return findViewById<View>(R.id.zxing_barcode_scanner) as DecoratedBarcodeView
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Do something here
            val resultIntent = Intent()
            resultIntent.putExtra("result", "back")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        Log.d("forus", "onResume")
        capture!!.onResume()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        recalcScannerScreenSize()

    }

    fun recalcScannerScreenSize(){
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val len: Float = width.toFloat() * 0.75f
        scannerLayout.layoutParams.width = len.toInt()
        scannerLayout.layoutParams.height = len.toInt()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(data != null) {
            val returnValue = data!!.getStringExtra("result")
            if(returnValue != null && returnValue=="back"){
                finish()
            }
        }



        val result = IntentIntegrator.parseActivityResult(requestCode,
                resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                //scan have an error
            } else {

                //decodeScanResult(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }



    override fun onPause() {
        super.onPause()
        Log.d("forus", "onResume")
        capture!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture!!.onSaveInstanceState(outState)
    }



    fun showToastMessage(message: String){
        if(hasWindowFocus())
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}