package io.forus.me.android.presentation.helpers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import io.forus.me.android.data.repository.settings.SettingsDataSource
import io.forus.me.android.presentation.crypt.CipherWrapper
import io.forus.me.android.presentation.crypt.CipherWrapper.Companion.TRANSFORMATION_ASYMMETRIC
import io.forus.me.android.presentation.crypt.KeyStoreWrapper
import java.security.Key


class AppSettings(private val context: Context) : SettingsDataSource {

    companion object {
        const val SETTINGS_FILENAME = "app-settings"

        const val FINGERPRINT_ENABLED = "FINGERPRINT_ENABLED"
        const val PINCODE_ENABLED = "PINCODE_ENABLED"
        const val PINCODE_ENCRYPTED = "PINCODE_ENCRYPTED"
        const val SEND_CRASH_REPORTS_ENABLED = "SEND_CRASH_REPORTS_ENABLED"
        const val FCM_TOKEN = "FCM_TOKEN"

        const val START_FROM_SCANNER_ENABLED = "START_FROM_SCANNER_ENABLED"
    }

    private val cipher = CipherWrapper(TRANSFORMATION_ASYMMETRIC)
    private var privateKey: Key
    private var publicKey: Key

    init {
        val keyStoreWrapper = KeyStoreWrapper(context)
        val store = keyStoreWrapper.getOrCreateAndroidKeyStoreAsymmetricKeyPair(SETTINGS_FILENAME)
        privateKey = store.private
        publicKey = store.public
    }

    private var sPref: SharedPreferences = context.getSharedPreferences(SETTINGS_FILENAME, MODE_PRIVATE)

    override fun clear() {
        sPref.edit().clear().commit()
    }

    override fun isFingerprintEnabled(): Boolean {
        return sPref.getBoolean(FINGERPRINT_ENABLED, false)
    }

    override fun setFingerprintEnabled(isFingerprintEnabled: Boolean): Boolean {
        return sPref.edit().putBoolean(FINGERPRINT_ENABLED, isFingerprintEnabled).commit()
    }

    override fun isPinEnabled(): Boolean {
        return sPref.getBoolean(PINCODE_ENABLED, false)
    }

    override fun setPin(pin: String): Boolean {
        val editor = sPref.edit()
        editor.putBoolean(PINCODE_ENABLED, pin != "")
        editor.putString(PINCODE_ENCRYPTED, if (pin != "") cipher.encrypt(pin, publicKey) else "")
        return editor.commit()
    }

    override fun getPin(): String {
        val pin = sPref.getString(PINCODE_ENCRYPTED, "")
        return if (pin != "") cipher.decrypt(pin?:"", privateKey) else ""
    }

    override fun setFCMToken(token: String): Boolean {
        return sPref.edit().putString(FCM_TOKEN, token).commit()
    }

    override fun getFCMToken(): String {
        return sPref.getString(FCM_TOKEN, "") ?: ""
    }

    override fun updateFCMToken(): Boolean {
        return setFCMToken("")
    }

    override fun isStartFromScannerEnabled(): Boolean {
        return sPref.getBoolean(START_FROM_SCANNER_ENABLED, false)
    }

    override fun setStartFromScannerEnabled(isEnabled: Boolean): Boolean {
        return sPref.edit().putBoolean(START_FROM_SCANNER_ENABLED, isEnabled).commit()
    }

    override fun isSendCrashReportsEnabled(): Boolean {
        return sPref.getBoolean(SEND_CRASH_REPORTS_ENABLED, false)
    }

    override fun setSendCrashReportsEnabled(isEnabled: Boolean): Boolean {
        return sPref.edit().putBoolean(SEND_CRASH_REPORTS_ENABLED, isEnabled).commit()
    }
}