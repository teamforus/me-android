package io.forus.me.android.presentation.view.screens.qr

import android.util.Log
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.navigation.Navigator
import io.forus.me.android.presentation.view.screens.qr.dialogs.ApproveValidationDialog
import io.forus.me.android.presentation.view.screens.qr.dialogs.RestoreIdentityDialog
import io.forus.me.android.presentation.view.screens.qr.dialogs.ScanVoucherNotEligibleDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class QrActionProcessor(private val scanner: QrScannerActivity,
                        private val recordsRepository: RecordsRepository,
                        private val accountRepository: AccountRepository,
                        private val vouchersRepository: VouchersRepository) {

    private val resources by lazy{
        return@lazy scanner.resources
    }

    private val reactivateDecoding by lazy{
        return@lazy scanner.reactivateDecoding
    }

    private fun reactivateDecoding() = reactivateDecoding.invoke()
    private fun showToastMessage(message: String) = scanner.showToastMessage(message)

    fun approveValidation(uuid: String){
        recordsRepository.readValidation(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { validation ->
                    if(validation.state  == Validation.State.pending && validation.uuid != null){
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
                    }
                    else onResultValidationAlreadyDone()
                }
                .onErrorReturn { onResultUnexpectedError() }
                .subscribe()
    }

    fun restoreIdentity(token: String){
        RestoreIdentityDialog(scanner,
                {
                    accountRepository.authorizeToken(token)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map { onResultIdentityRestored() }
                            .onErrorReturn {
                                if(it is RetrofitException && it.kind == RetrofitException.Kind.HTTP && it.responseCode == 402){
                                    onResultTokenExpired()
                                }
                                else onResultUnexpectedError()
                            }
                            .subscribe()
                },
                reactivateDecoding)
                .show()
    }

    fun scanVoucher(address: String){
        vouchersRepository.getVoucherAsProvider(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    if(!it.voucher.isProduct && it.allowedOrganizations.isEmpty()){
                        ScanVoucherNotEligibleDialog(scanner, reactivateDecoding).show()
                    }
                    else{
                        onResultVoucherScanned(address)
                    }
                }
                .onErrorReturn {
                    //Log.e("QR_ACTION", "scan voucher", it)
                    ScanVoucherNotEligibleDialog(scanner, reactivateDecoding).show()
                }
                .subscribe()
    }

    fun unknownQr(){
        showToastMessage(resources.getString(R.string.qr_unknown_type))
        reactivateDecoding()
    }

    private fun onResultValidationApproved(){
        showToastMessage(resources.getString(R.string.qr_validation_approved))
        reactivateDecoding()
    }

    private fun onResultValidationDeclined(){
        showToastMessage(resources.getString(R.string.qr_validation_declined))
        reactivateDecoding()
    }

    private fun onResultValidationAlreadyDone(){
        showToastMessage(resources.getString(R.string.qr_validation_already_done))
        reactivateDecoding()
    }

    private fun onResultIdentityRestored(){
        showToastMessage(resources.getString(R.string.qr_identity_restored))
        (android.os.Handler()).postDelayed({
            reactivateDecoding()
        },1000)
    }

    private fun onResultTokenExpired(){
        showToastMessage(resources.getString(R.string.qr_identity_expired))
        reactivateDecoding()
    }

    private fun onResultVoucherScanned(address: String){
        showToastMessage(resources.getString(R.string.qr_voucher_scanned))
        Navigator().navigateToVoucherProvider(scanner, address)
        (android.os.Handler()).postDelayed({
            reactivateDecoding()
        },1000)
    }

    private fun onResultUnexpectedError(){
        showToastMessage(resources.getString(R.string.qr_unexpected_error))
        reactivateDecoding()
    }

}