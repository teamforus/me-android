package io.forus.me.android.data.repository.validators.datasource

import io.forus.me.android.data.entity.validators.response.Validator
import io.reactivex.Observable

interface ValidatorsDataSource {

    fun listAllValidators(): Observable<List<Validator>>

}