package io.forus.me.android.data.repository.validators

import io.forus.me.android.data.repository.validators.datasource.ValidatorsDataSource
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.domain.repository.validators.ValidatorsRepository
import io.reactivex.Observable
import io.reactivex.Single

class ValidatorsRepository(private val validatorsDataSource: ValidatorsDataSource) : ValidatorsRepository {

    val SAMPLE_LOGO_URL = "https://emojipedia-us.s3.amazonaws.com/thumbs/120/apple/129/male-pilot_1f468-200d-2708-fe0f.png"
    val validators: MutableList<SimpleValidator> = mutableListOf()

    init {
        validators.add(SimpleValidator(0, "DigiD", "Open DigiD app", SAMPLE_LOGO_URL))
        validators.add(SimpleValidator(1, "Gemeente Zuidhorn", "Automatische validatie", SAMPLE_LOGO_URL))
        validators.add(SimpleValidator(2, "Gemeente Zuidhorn", "Verzoek validatie", SAMPLE_LOGO_URL))
    }

    override fun getValidators(): Observable<List<SimpleValidator>> {
        return validatorsDataSource.listAllValidators()
                .map { it.map { SimpleValidator(it.id, it.organization?.name ?: "", it.email ?: "", it.organization.logo ?: SAMPLE_LOGO_URL) } }
    }

    override fun getValidator(validatorId: Long): Observable<SimpleValidator> {
        return getValidators().flatMap {
            val item: SimpleValidator? = it.find{ it -> it.id == validatorId}
            if(item != null) Single.just(item).toObservable()
            else Observable.error(Exception("Not found"))
        }
    }

}