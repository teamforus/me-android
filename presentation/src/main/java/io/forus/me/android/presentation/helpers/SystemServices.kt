package io.forus.me.android.presentation.helpers



import android.annotation.TargetApi
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.os.Handler
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.appcompat.app.AlertDialog
import io.forus.me.android.presentation.BuildConfig
import io.forus.me.android.presentation.R

@TargetApi(Build.VERSION_CODES.M)
class SystemServices(private val context: Context) {

    companion object {
        fun hasMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private val keyguardManager: KeyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    /**
     * There is a nice [FingerprintManagerCompat] class that makes all dirty work for us, but as always, shit happens.
     * Behind the scenes it is using `Context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)`
     * method, that is returning false on 23 API emulators, when in fact [FingerprintManager] is there and is working fine.
     */
    private var fingerprintManager: FingerprintManager? = null

    init {
        if (hasMarshmallow()) {
            fingerprintManager = context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager?
        }
    }


    fun isFingerprintHardwareAvailable() = fingerprintManager?.isHardwareDetected ?: false

    fun hasEnrolledFingerprints() = fingerprintManager?.hasEnrolledFingerprints() ?: false



}