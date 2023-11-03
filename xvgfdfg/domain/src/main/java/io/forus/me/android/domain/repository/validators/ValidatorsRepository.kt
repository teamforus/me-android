package io.forus.me.android.domain.repository.validators

import io.forus.me.android.domain.models.validators.SimpleValidator
import io.reactivex.Observable

interface ValidatorsRepository{

    fun getValidators(): Observable<List<SimpleValidator>>

    fun getValidators(recordId: Long): Observable<List<SimpleValidator>>

    fun getValidator(validatorId: Long): Observable<SimpleValidator>

    fun requestValidation(recordId: Long, validatorId: Long): Observable<Boolean>

    fun requestValidations(recordId: Long, validatorIds: List<Long>): Observable<Boolean>
}