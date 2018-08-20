package io.forus.me.android.data.repository.records

import io.forus.me.android.domain.models.records.Validator
import io.reactivex.Observable
import io.reactivex.Single

class ValidationRepository : io.forus.me.android.domain.repository.records.ValidationRepository {

    val validators: MutableList<Validator> = mutableListOf()

    init {
        validators.add(Validator(0, "DigiD", "Open DigiD app", "https://emojipedia-us.s3.amazonaws.com/thumbs/120/apple/129/male-pilot_1f468-200d-2708-fe0f.png"))
        validators.add(Validator(1, "Gemeente Zuidhorn", "Automatische validatie", "https://emojipedia-us.s3.amazonaws.com/thumbs/120/apple/129/male-pilot_1f468-200d-2708-fe0f.png"))
        validators.add(Validator(2, "Gemeente Zuidhorn", "Verzoek validatie", "https://emojipedia-us.s3.amazonaws.com/thumbs/120/apple/129/male-pilot_1f468-200d-2708-fe0f.png"))

        for(c in 3..10){
            validators.add(Validator(c.toLong(), "Validator ("+c+")", "title",""))
        }
    }

    override fun getValidators(): Observable<List<Validator>> {
        return Single.just(validators.toList()).toObservable()
    }

    override fun getValidator(validatorId: Long): Observable<Validator> {
        val item: Validator? = validators.find{it -> it.id == validatorId}
        if(item != null) return Single.just(item).toObservable()
        else return Observable.error(Exception("Not found"))
    }

}