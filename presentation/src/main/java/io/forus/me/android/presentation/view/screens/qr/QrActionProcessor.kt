package io.forus.me.android.presentation.view.screens.qr

import android.util.Log
import io.forus.me.android.data.repository.settings.SettingsDataSource
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.navigation.Navigator
import io.forus.me.android.presentation.view.screens.qr.dialogs.ApproveValidationDialog
import io.forus.me.android.presentation.view.screens.qr.dialogs.RestoreIdentityDialog
import io.forus.me.android.presentation.view.screens.qr.dialogs.ScanVoucherEmptyDialog
import io.forus.me.android.presentation.view.screens.qr.dialogs.ScanVoucherNotEligibleDialog
import io.forus.me.android.presentation.view.screens.vouchers.provider.ProviderActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

class QrActionProcessor(private val scanner: QrScannerActivity,
                        private val recordsRepository: RecordsRepository,
                        private val accountRepository: AccountRepository,
                        private val vouchersRepository: VouchersRepository,
                        private val settingsDataSource: SettingsDataSource) {

    private val resources by lazy {
        return@lazy scanner.resources
    }

    private val reactivateDecoding by lazy {
        return@lazy scanner.reactivateDecoding
    }

    private val navigator: Navigator by lazy(LazyThreadSafetyMode.NONE) { Navigator() }

    private fun reactivateDecoding() = reactivateDecoding.invoke()
    private fun showToastMessage(message: String) = scanner.showToastMessage(message)

    fun approveValidation(uuid: String) {
        recordsRepository.readValidation(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { validation ->
                    if (validation.state == Validation.State.pending && validation.uuid != null) {
                        if (scanner.hasWindowFocus())
                            ApproveValidationDialog(scanner,
                                    validation,
                                    {
                                        recordsRepository.approveValidation(validation.uuid!!)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .map { onResultValidationApproved() }
                                                .onErrorReturn { onResultUnexpectedError() }
                                                .subscribe()
                                    },
                                    {
                                        recordsRepository.declineValidation(validation.uuid!!)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .map { onResultValidationDeclined() }
                                                .onErrorReturn { onResultUnexpectedError() }
                                                .subscribe()
                                    },
                                    reactivateDecoding)
                                    .show()
                    } else onResultValidationAlreadyDone()
                }
                .onErrorReturn { onResultUnexpectedError() }
                .subscribe()
    }

    fun restoreIdentity(token: String) {
        if (scanner.hasWindowFocus())
            RestoreIdentityDialog(scanner,
                    {
                        accountRepository.authorizeToken(token)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .map { onResultIdentityRestored() }
                                .onErrorReturn {
                                    if (it is RetrofitException && it.kind == RetrofitException.Kind.HTTP && it.responseCode == 402) {
                                        onResultTokenExpired()
                                    } else onResultUnexpectedError()
                                }
                                .subscribe()
                    },
                    reactivateDecoding)
                    .show()
    }

    fun scanVoucher(address: String) {
        vouchersRepository.getVoucherAsProvider(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    if (it?.voucher?.isProduct != true && it.allowedOrganizations.isEmpty()) {
                        if (scanner.hasWindowFocus())
                            ScanVoucherNotEligibleDialog(scanner, reactivateDecoding).show()
                    } else if (it.voucher.isProduct != true && (it.voucher.amount ?: 0.toBigDecimal()).compareTo(BigDecimal.ZERO) == 0) {
                        if (scanner.hasWindowFocus())
                            ScanVoucherEmptyDialog(scanner, reactivateDecoding).show()
                    } else {
                        onResultVoucherScanned(address)
                    }
                }
                .onErrorReturn {
                    Log.e("QR_ACTION", "scan voucher_error", it)
                    if (scanner.hasWindowFocus())
                        ScanVoucherNotEligibleDialog(scanner, reactivateDecoding).show()
                }
                .subscribe()
    }

    fun unknownQr() {
        showToastMessage(resources.getString(R.string.qr_unknown_type))
        reactivateDecoding()
    }

    private fun onResultValidationApproved() {
        showToastMessage(resources.getString(R.string.qr_validation_approved))
        reactivateDecoding()
    }

    private fun onResultValidationDeclined() {
        showToastMessage(resources.getString(R.string.qr_validation_declined))
        reactivateDecoding()
    }

    private fun onResultValidationAlreadyDone() {
        showToastMessage(resources.getString(R.string.qr_validation_already_done))
        reactivateDecoding()
    }

    private fun onResultIdentityRestored() {
        showToastMessage(resources.getString(R.string.qr_identity_restored))
        (android.os.Handler()).postDelayed({
            reactivateDecoding()
        }, 1000)
    }

    private fun onResultTokenExpired() {
        showToastMessage(resources.getString(R.string.qr_identity_expired))
        reactivateDecoding()
    }

    private fun onResultVoucherScanned(address: String) {
        // Hide finger print on voucher
        if (false and settingsDataSource.isPinEnabled() ) {
            // Database will be opened later
            val useFingerprint = settingsDataSource.isFingerprintEnabled()
            navigator.navigateToCheckTransactionPin(scanner, ProviderActivity.getCallingIntent(scanner, address), useFingerprint)
            (android.os.Handler()).postDelayed({
                reactivateDecoding()
            }, 1000)
        } else {
            showToastMessage(resources.getString(R.string.qr_voucher_scanned))
            navigator.navigateToVoucherProvider(scanner, address)
            (android.os.Handler()).postDelayed({
                reactivateDecoding()
            }, 1000)
        }
    }

    private fun onResultUnexpectedError() {
        showToastMessage(resources.getString(R.string.qr_unexpected_error))
        reactivateDecoding()
    }

}