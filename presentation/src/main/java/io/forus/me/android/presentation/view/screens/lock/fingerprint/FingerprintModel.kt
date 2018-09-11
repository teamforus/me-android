package io.forus.me.android.presentation.view.screens.lock.fingerprint

data class FingerprintModel(

        val unlockSuccess: Boolean = false,
        val unlockFail: Boolean = false,
        val unlockFailMessage: String? = null,
        val usePin: Boolean = false
)