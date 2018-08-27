package io.forus.me.android.presentation.view.screens.qr

sealed class QrDecoderResult {

    data class ValidationApproved(val success: Boolean) : QrDecoderResult()

    data class IdentityRestored(val success: Boolean) : QrDecoderResult()

    data class UnknownQr(val error: Throwable) : QrDecoderResult()

    data class UnexpectedError(val error: Throwable) : QrDecoderResult()

}

