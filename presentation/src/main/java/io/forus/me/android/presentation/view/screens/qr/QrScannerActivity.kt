package io.forus.me.android.presentation.view.screens.qr

//import com.dlazaro66.qrcodereaderview.QRCodeReaderView
//import com.google.zxing.integration.android.IntentIntegrator
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.qr.QrDecoderResult
import io.forus.me.android.presentation.view.component.qr.PointsOverlayView
import kotlinx.android.synthetic.main.activity_qr_decoder.*
import kotlinx.android.synthetic.main.activity_qr_decoder.main_layout
import kotlinx.android.synthetic.main.activity_qr_decoder_content.*
import java.util.concurrent.atomic.AtomicBoolean


class QrScannerActivity : FragmentActivity(), QRCodeReaderView.OnQRCodeReadListener /*, QRCodeReaderView.OnQRCodeReadListener*/ {


    private val MY_PERMISSION_REQUEST_CAMERA = 0

    private var pointsOverlayView: PointsOverlayView? = null

    private var qrDecoder = Injection.instance.qrDecoder
    private var qrActionProcessor = QrActionProcessor(this, Injection.instance.recordsRepository, Injection.instance.accountRepository, Injection.instance.vouchersRepository, Injection.instance.settingsDataSource)

    private var decodingInProgress = AtomicBoolean()
    var allowReactivate = {
        decodingInProgress.set(false);
        Unit
    }
    var reactivateDecoding = {
        decodingInProgress.set(false);
        if(qrCodeReaderView == null) {
            initQRCodeReaderView()
        }
        qrCodeReaderView!!.startCamera()
        Unit
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, QrScannerActivity::class.java)
        }
    }


    private var qrCodeReaderView: QRCodeReaderView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_qr_decoder)

    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (!decodingInProgress.get()) {
                reactivateDecoding()
            }
        } else {
            requestCameraPermission()
        }

    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
            return
        }

        if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(main_layout, resources.getString(R.string.qr_camera_permission_granted), Snackbar.LENGTH_SHORT).show()
            initQRCodeReaderView()
        } else {

            requestCameraPermission()

        }
    }


    fun decodeScanResult(text: String) {

        qrCodeReaderView!!.stopCamera()
        decodingInProgress.set(true)
        val result = qrDecoder.decode(text)


        when (result) {
            is QrDecoderResult.ApproveValidation -> qrActionProcessor.approveValidation(result.uuid)
            is QrDecoderResult.RestoreIdentity -> qrActionProcessor.restoreIdentity(result.token)
            is QrDecoderResult.ScanVoucher -> qrActionProcessor.scanVoucher(result.address)
            is QrDecoderResult.UnknownQr -> {
                if (text.length == 12) {
                    try {
                        val oldVoucherToken = text.toLong()
                        qrActionProcessor.scanVoucher(oldVoucherToken.toString())
                    } catch (e: Exception) {
                        qrActionProcessor.unknownQr()
                    }
                } else {
                    qrActionProcessor.unknownQr()
                }

            }
            is QrDecoderResult.DemoVoucher -> qrActionProcessor.scanVoucher(result.testToken, true)
        }
    }


    fun showToastMessage(message: String) {
        if (hasWindowFocus())
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Snackbar.make(main_layout, resources.getString(R.string.qr_camera_access_required),
                    Snackbar.LENGTH_INDEFINITE).setAction("OK") { ActivityCompat.requestPermissions(this@QrScannerActivity, arrayOf(Manifest.permission.CAMERA), MY_PERMISSION_REQUEST_CAMERA) }.show()
        } else {
            Snackbar.make(main_layout, resources.getString(R.string.qr_camera_permission_not_available),
                    Snackbar.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSION_REQUEST_CAMERA)
        }
    }

    // var integrator : IntentIntegrator? = null

    private fun initQRCodeReaderView() {

        val content = layoutInflater.inflate(R.layout.activity_qr_decoder_content, main_layout, true)

        //qrCodeReaderView = content.findViewById(R.id.qrdecoderview)
        pointsOverlayView = content.findViewById(R.id.points_overlay_view)

        qrCodeReaderView = content.findViewById(R.id.qrCodeReaderView);
        initQRView()

        Snackbar.make(main_layout, resources.getString(R.string.qr_scan_help), Snackbar.LENGTH_LONG)
                .show()
    }


    fun initQRView() {
        qrCodeReaderView!!.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView!!.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView!!.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView!!.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView!!.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView!!.setBackCamera();


    }

    val h = Handler()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (data != null) {
            val returnValue = data!!.getStringExtra("result")
            if (returnValue != null && returnValue == "back") {
                finish()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Do something here
            this@QrScannerActivity.finish()
            true
        } else super.onKeyDown(keyCode, event)
    }

    var active = false

    override fun onStart() {
        super.onStart()
        active = true
    }

    override fun onStop() {
        super.onStop()
        active = false
    }

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        Log.d("forusQR", "Result = $text")
        if (text != null) {
            decodeScanResult(text)
        }
    }

}