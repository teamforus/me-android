package io.forus.me.android.data.repository.validators.datasource.remote

import io.forus.me.android.data.entity.validators.response.Validator
import io.forus.me.android.data.net.validators.ValidatorsService
import io.forus.me.android.data.repository.datasource.RemoteDataSource
import io.forus.me.android.data.repository.validators.datasource.ValidatorsDataSource
import io.reactivex.Observable

class ValidatorsRemoteDataSource(f: () -> ValidatorsService): ValidatorsDataSource, RemoteDataSource<ValidatorsService>(f) {

    override fun listAllValidators(): Observable<List<Validator>> = service.listAllValidators()
            .map { it -> it.data ?: listOf()}

}