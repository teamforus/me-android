package io.forus.me.android.presentation.view.screens.qr

import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.view.screens.qr.dialogs.ApproveValidationDialog
import io.forus.me.android.presentation.view.screens.qr.dialogs.RestoreIdentityDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class QrActionProcessor(private val scanner: QrScannerActivity, private val recordsRepository: RecordsRepository, private val accountRepository: AccountRepository, private val vouchersRepository: VouchersRepository) {

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
                                            .map { scanner.onResultValidationApproved() }
                                            .onErrorReturn { scanner.onResultUnexpectedError() }
                                            .subscribe()
                                },
                                {
                                    recordsRepository.declineValidation(validation.uuid!!)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .map { scanner.onResultValidationDeclined() }
                                            .onErrorReturn { scanner.onResultUnexpectedError() }
                                            .subscribe()
                                },
                                scanner.reactivateDecoding)
                                .show()
                    }
                    else scanner.onResultValidationAlreadyDone()
                }
                .onErrorReturn { scanner.onResultUnexpectedError() }
                .subscribe()
    }

    fun restoreIdentity(token: String){
        RestoreIdentityDialog(scanner,
                {
                    accountRepository.authorizeToken(token)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map { scanner.onResultIdentityRestored() }
                            .onErrorReturn {
                                if(it is RetrofitException && it.kind == RetrofitException.Kind.HTTP && it.responseCode == 402){
                                    scanner.onResultTokenExpired()
                                }
                                else scanner.onResultUnexpectedError()
                            }
                            .subscribe()
                },
                scanner.reactivateDecoding)
                .show()
    }

    fun scanVoucher(address: String){
        vouchersRepository.getVoucherAsProvider(address)
                .map { scanner.onResultVoucherScanned(address)}
                .onErrorReturn { scanner.onResultVoucherAccessDenied() }
                .subscribe()
    }

}