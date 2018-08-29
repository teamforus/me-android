package io.forus.me.android.presentation.view.screens.qr

import com.google.gson.GsonBuilder
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.models.qr.QrCode
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.reactivex.Observable
import io.reactivex.Single

class QrDecoder(private val accountRepository: AccountRepository, private val recordsRepository: RecordsRepository){

    private val gson = GsonBuilder().create()

    fun decode(text: String): Single<QrDecoderResult> {

        try {
            val qr: QrCode = gson.fromJson(text, QrCode::class.java)

            return when(qr.type){
                QrCode.Type.AUTH_TOKEN -> decodeRestoreIdentity(qr.value)
                QrCode.Type.VOUCHER -> Single.just(QrDecoderResult.UnknownQr(UnsupportedOperationException("Not implemented")))
                QrCode.Type.P2P_RECORD -> decodeApproveValidation(qr.value)
                QrCode.Type.P2P_IDENTITY -> Single.just(QrDecoderResult.UnknownQr(UnsupportedOperationException("Not implemented")))
            }
        }
        catch (e: Exception){
            return Single.just(QrDecoderResult.UnknownQr(e))
        }
    }

    fun decodeApproveValidation(value: String): Single<QrDecoderResult> {
        return Single.fromObservable(recordsRepository.readValidation(value)
                .flatMap <QrDecoderResult> {
                    if(it.state  == Validation.State.pending){
                        recordsRepository.approveValidation(value)
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
                    QrDecoderResult.UnexpectedError(it)
                }
        )
    }

    fun decodeRestoreIdentity(value: String): Single<QrDecoderResult> {
        return Single.fromObservable(accountRepository.authorizeToken(value)
                .map <QrDecoderResult> {
                    QrDecoderResult.IdentityRestored(it)
                }
                .onErrorReturn {
                    if(it is RetrofitException && it.kind == RetrofitException.Kind.HTTP && it.responseCode == 402){
                        QrDecoderResult.IdentityRestored(false)
                    }
                    else QrDecoderResult.UnexpectedError(it)
                }
        )
    }
}