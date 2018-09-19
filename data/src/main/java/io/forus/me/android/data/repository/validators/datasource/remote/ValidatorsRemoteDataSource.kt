package io.forus.me.android.data.repository.validators.datasource.remote

import io.forus.me.android.data.entity.validators.request.CreateValidationRequest
import io.forus.me.android.data.entity.validators.response.ValidationRequest
import io.forus.me.android.data.entity.validators.response.Validator
import io.forus.me.android.data.net.validators.ValidatorsService
import io.forus.me.android.data.repository.datasource.RemoteDataSource
import io.forus.me.android.data.repository.validators.datasource.ValidatorsDataSource
import io.reactivex.Observable

class ValidatorsRemoteDataSource(f: () -> ValidatorsService): ValidatorsDataSource, RemoteDataSource<ValidatorsService>(f) {

    override fun listAllValidators(): Observable<List<Validator>> = service.listAllValidators()
            .map { it -> it.data ?: listOf()}

    override fun listAllValidationRequests(): Observable<List<ValidationRequest>> = service.listAllValidationRequests()
            .map { it -> it.data ?: listOf()}

    override fun createValidationRequest(recordId: Long, validatorId: Long): Observable<ValidationRequest> = service.createValidationRequest(CreateValidationRequest(validatorId, recordId))
}