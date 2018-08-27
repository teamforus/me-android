package io.forus.me.android.presentation.view.screens.qr

import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.reactivex.Observable
import io.reactivex.Single

class QrDecoder(private val accountRepository: AccountRepository, private val recordsRepository: RecordsRepository){

    fun decode(text: String): Single<QrDecoderResult> {
        return decodeApproveValidation(text)
                .flatMap <QrDecoderResult>{
                    if(it is QrDecoderResult.UnknownQr) decodeRestoreIdentity(text)
                    else Single.just(it)
                }
    }

    fun decodeApproveValidation(text: String): Single<QrDecoderResult> {
        return Single.fromObservable(recordsRepository.readValidation(text)
                .flatMap <QrDecoderResult> {
                    if(it.state  == Validation.State.pending){
                        recordsRepository.approveValidation(text)
                                .map <QrDecoderResult> { QrDecoderResult.ValidationApproved(it) }
                                .onErrorReturn {
                                    if(it is RetrofitException && it.kind == RetrofitException.Kind.HTTP && it.responseCode == 403){
                                        QrDecoderResult.ValidationApproved(false)
                                    }
                                    QrDecoderResult.UnexpectedError(it)
                                }
                    }
                    else{
                        Observable.just(QrDecoderResult.ValidationApproved(false))
                    }
                }
                .onErrorReturn {
                    if(it is RetrofitException && it.kind == RetrofitException.Kind.HTTP && it.responseCode == 404){
                        QrDecoderResult.UnknownQr(it)
                    }
                    else QrDecoderResult.UnexpectedError(it)
                }
        )
    }

    fun decodeRestoreIdentity(text: String): Single<QrDecoderResult> {
        return Single.fromObservable(accountRepository.authorizeToken(text)
                .map <QrDecoderResult> {
                    QrDecoderResult.IdentityRestored(it)
                }
                .onErrorReturn {
                    if(it is RetrofitException && it.kind == RetrofitException.Kind.HTTP){
                        when(it.responseCode){
                            402 -> QrDecoderResult.IdentityRestored(false)
                            404 -> QrDecoderResult.UnknownQr(it)
                            else -> QrDecoderResult.UnexpectedError(it)
                        }
                    }
                    else QrDecoderResult.UnexpectedError(it)
                }
        )
    }
}