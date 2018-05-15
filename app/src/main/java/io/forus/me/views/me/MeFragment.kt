package io.forus.me.views.me

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.journeyapps.barcodescanner.BarcodeCallback
import io.forus.me.R
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView
import io.forus.me.IdentityViewActivity
import io.forus.me.entities.base.EthereumItem
import io.forus.me.views.base.TitledFragment
import kotlinx.android.synthetic.main.me_fragment.*
import kotlinx.android.synthetic.main.my_qr_view.*


/**
 * Created by martijn.doornik on 15/03/2018.
 */
class MeFragment : TitledFragment() {
    private lateinit var qrListener: QrListener
    private lateinit var scanner: BarcodeView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.me_fragment, container, false)
        this.scanner = view.findViewById(R.id.qrScanner)
        requestPermission()
        scanner.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                pauseScanner()
                qrListener.onQrResult(result.text)
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
                //setMarkers(resultPoints)
            }
        })
        val button: ImageView = view.findViewById(R.id.myIdentitiesButton)
        button.setOnClickListener {
            if (it == myIdentitiesButton) {
                toIdentitiesView()
            }
        }
        return view
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
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.CAMERA), 0)
        }
    }

    fun resumeScanner() {
        scanner.resume()
    }

    fun setMarkers(resultPoints: List<ResultPoint>) {
        if (this.view != null && this.context != null) {
            val markerView: RelativeLayout? = view!!.findViewById(R.id.markerContainer)
            if (markerView != null) {
                markerView.removeAllViews()
                val height = markerView.height
                for (resultPoint in resultPoints) {
                    val marker = ImageView(this.context!!)
                    marker.setBackgroundColor(ContextCompat.getColor(this.context!!, R.color.red))
                    val params = RelativeLayout.LayoutParams(10, 10)
                    params.leftMargin = (resultPoint.x-5+110).toInt()
                    params.topMargin = (resultPoint.y-5+(height/4)).toInt()
                    markerView.addView(marker, params)
                }
            }
        }
    }

    fun toIdentitiesView() {
        val intent = Intent(this.context, IdentityViewActivity::class.java)
        startActivity(intent)
    }

    fun with(listener: QrListener): MeFragment {
        this.qrListener = listener
        return this
    }

    interface QrListener {
        fun onQrResult(result:String)
    }
}