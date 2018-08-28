package io.forus.me.android.data.repository.validators

import io.forus.me.android.data.repository.validators.datasource.ValidatorsDataSource
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.domain.repository.validators.ValidatorsRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.functions.Function

class ValidatorsRepository(private val validatorsDataSource: ValidatorsDataSource) : ValidatorsRepository {

    val SAMPLE_LOGO_URL = "https://emojipedia-us.s3.amazonaws.com/thumbs/120/apple/129/male-pilot_1f468-200d-2708-fe0f.png"

    override fun getValidators(): Observable<List<SimpleValidator>> {
        return validatorsDataSource.listAllValidators()
                .map { it.map { SimpleValidator(it.id, it.organization.id, it.organization.name, it.email, it.organization.logo ?: SAMPLE_LOGO_URL) } }
    }

    override fun getValidators(recordId: Long): Observable<List<SimpleValidator>> {
        return validatorsDataSource.listAllValidationRequests()
                .map { it.filter{ it.recordId == recordId && it.validator?.id != null} }
                .map { it.map { SimpleValidator(it.validator.id, it.validator.organization.id, it.validator.organization.name, it.validator.email,
                        it.validator.organization?.logo ?: SAMPLE_LOGO_URL, SimpleValidator.Status.valueOf(it.state.toString()))}}
    }

    override fun getValidator(validatorId: Long): Observable<SimpleValidator> {
        return getValidators().flatMap {
            val item: SimpleValidator? = it.find{ it -> it.id == validatorId}
            if(item != null) Single.just(item).toObservable()
            else Observable.error(Exception("Not found"))
        }
    }

    override fun requestValidation(recordId: Long, validatorId: Long): Observable<Boolean> {
            return validatorsDataSource.createValidationRequest(recordId, validatorId).map{ true }
    }

    override fun requestValidations(recordId: Long, validatorIds: List<Long>): Observable<Boolean> {
        val singles: MutableList<SingleSource<Boolean>> = mutableListOf()
        for(validatorId in validatorIds){
            singles.add(Single.fromObservable(requestValidation(recordId, validatorId)))
        }
        return Single.zip(singles) { true }.toObservable()
    }

}