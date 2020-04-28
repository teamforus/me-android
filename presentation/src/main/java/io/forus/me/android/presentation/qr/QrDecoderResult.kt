package io.forus.me.android.presentation.qr

sealed class QrDecoderResult {

    data class ApproveValidation(val uuid: String) : QrDecoderResult()

    data class RestoreIdentity(val token: String) : QrDecoderResult()

    data class ScanVoucher(val address: String) : QrDecoderResult()

    data class UnknownQr(val error: Throwable) : QrDecoderResult()

    data class DemoVoucher(val testToken: String) : QrDecoderResult()

}

