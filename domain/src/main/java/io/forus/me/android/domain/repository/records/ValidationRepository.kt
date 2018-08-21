package io.forus.me.android.domain.repository.records

import io.forus.me.android.domain.models.records.Validator
import io.reactivex.Observable

interface ValidationRepository {

    fun getValidators(): Observable<List<Validator>>

    fun getValidator(validatorId: Long): Observable<Validator>

}