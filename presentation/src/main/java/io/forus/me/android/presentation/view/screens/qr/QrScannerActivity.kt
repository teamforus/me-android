package io.forus.me.android.presentation.view.screens.qr

//import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.qr.QrDecoderResult
import io.forus.me.android.presentation.view.component.qr.PointsOverlayView
import kotlinx.android.synthetic.main.activity_qr_decoder.*
import java.util.concurrent.atomic.AtomicBoolean


class QrScannerActivity : FragmentActivity()/*, QRCodeReaderView.OnQRCodeReadListener*/ {


    private val MY_PERMISSION_REQUEST_CAMERA = 0

    //private var qrCodeReaderView: QRCodeReaderView? = null
    private var pointsOverlayView: PointsOverlayView? = null

    private var qrDecoder = Injection.instance.qrDecoder
    private var qrActionProcessor = QrActionProcessor(this, Injection.instance.recordsRepository, Injection.instance.accountRepository, Injection.instance.vouchersRepository, Injection.instance.settingsDataSource)

    private var decodingInProgress = AtomicBoolean()
    var reactivateDecoding = { decodingInProgress.set(false);
        //qrCodeReaderView?.setQRDecodingEnabled(true);
        Unit }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, QrScannerActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_qr_decoder)



    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView()
        } else {
            requestCameraPermission()
        }
       // qrCodeReaderView?.setQRDecodingEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

        //qrCodeReaderView?.setQRDecodingEnabled(false)
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
//            Snackbar.make(main_layout, resources.getString(R.string.qr_camera_permission_denied), Snackbar.LENGTH_SHORT)
//                    .show()

            requestCameraPermission()

        }
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
    /*override fun onQRCodeRead(text: String, points: Array<PointF>) {

        if(decodingInProgress.compareAndSet(false, true)){

            qrCodeReaderView?.setQRDecodingEnabled(false)
            //pointsOverlayView?.setPoints(points)


        }
    }*/

    fun decodeScanResult(text: String){
        Log.d("forus", "Scanned_text $text")

        val result = qrDecoder.decode(text)


        when(result){
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


    fun showToastMessage(message: String){
        if(hasWindowFocus())
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

    var integrator : IntentIntegrator? = null

    private fun initQRCodeReaderView() {
        val content = layoutInflater.inflate(R.layout.activity_qr_decoder_content, main_layout, true)

        //qrCodeReaderView = content.findViewById(R.id.qrdecoderview)
        pointsOverlayView = content.findViewById(R.id.points_overlay_view)

       // val zxing_barcode_scanner: com.journeyapps.barcodescanner.DecoratedBarcodeView  = content.findViewById(R.id.zxing_barcode_scanner)
       // zxing_barcode_scanner.resume()




       /* qrCodeReaderView?.setAutofocusInterval(1500L)
        qrCodeReaderView?.setOnQRCodeReadListener(this)
        qrCodeReaderView?.setBackCamera()
        qrCodeReaderView?.startCamera()*/

         integrator = IntentIntegrator(this);
        integrator!!.setOrientationLocked(true);
        integrator!!.setCaptureActivity(CaptureActivityPortrait::class.java)
        integrator!!.initiateScan();



       /* val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.PDF_417)
        integrator.setPrompt("Scan a barcode")
        //integrator.setCameraId(0) // Use a specific camera of the device

        integrator.setOrientationLocked(true)
        integrator.setBeepEnabled(true)
        integrator.setCaptureActivity(CaptureActivityPortrait::class.java)
        integrator.initiateScan()*/


        Snackbar.make(main_layout, resources.getString(R.string.qr_scan_help), Snackbar.LENGTH_LONG)
                .show()
    }

    val h = Handler()

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

                decodeScanResult(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
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

}