package io.forus.me.android.data.repository.records

import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.forus.me.android.domain.models.records.*
import io.forus.me.android.domain.models.vouchers.Organization
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3

class RecordsRepository(private val recordsRemoteDataSource: RecordsDataSource) : io.forus.me.android.domain.repository.records.RecordsRepository {

    private val defaultCategoryNames: List<String> = listOf("Persoonlijk", "Medical", "Zakelijk", "Relaties", "Certificaten", "Anderen")

    override fun getRecordTypes(): Observable<List<RecordType>> {
        return recordsRemoteDataSource.getRecordTypes()
                .map { recordTypes -> recordTypes.map { recordType -> RecordType(recordType.key ?: "", recordType.type ?: "", recordType.name ?: "") }}
    }

    override fun getCategories(): Observable<List<RecordCategory>> {

        return recordsRemoteDataSource.getRecordCategories()
                .flatMap { categories ->
                    val newCategoryNames: MutableList<String> = mutableListOf()
                    defaultCategoryNames.forEach{ name ->
                        if(!categories.map{it.name}.contains(name)){
                            newCategoryNames.add(name)
                        }
                    }
                    if(newCategoryNames.isNotEmpty()){
                        newCategoryNames.forEach {
                            newCategory(NewRecordCategoryRequest(it, 0)).blockingSubscribe()
                        }
                        getCategories()
                    }
                    else{
                        Observable.just(categories.map { RecordCategory(it.id, it.name, it.order, 0) }
                        )
                    }
                }
    }

    override fun getCategoriesWithRecordCount(): Observable<List<RecordCategory>> {
        return getCategories().flatMap { categories ->
            val result: MutableList<RecordCategory> = mutableListOf()
            categories.forEach{
                getRecordsCount(it.id).blockingSubscribe{recordsCount ->
                    result.add(RecordCategory(it.id, it.name, it.order, recordsCount))
                }
            }
            Observable.just(result)
        }
    }

    override fun newCategory(newRecordCategoryRequest: NewRecordCategoryRequest): Observable<Boolean> {
        return recordsRemoteDataSource.createRecordCategory(io.forus.me.android.data.entity.records.request.CreateCategory(newRecordCategoryRequest.order, newRecordCategoryRequest.name))
                .map { true }
    }

    override fun getCategory(categoryId: Long?): Observable<RecordCategory> {
        return if(categoryId == null) Observable.just(RecordCategory(-1, "null category", 0))
        else recordsRemoteDataSource.retrieveRecordCategory(categoryId).map{ RecordCategory(it.id, it.name, it.order)}
    }

    override fun getRecordsCount(recordCategoryId: Long): Observable<Long> {
        return recordsRemoteDataSource.getRecords(recordCategoryId).map {it.size.toLong()}
    }

    override fun getRecords(): Observable<List<Record>> {
        return Single.zip(
                Single.fromObservable(getCategories()),
                Single.fromObservable(getRecordTypes()),
                Single.fromObservable(recordsRemoteDataSource.getRecords()),
                Function3 { categories: List<RecordCategory>, types: List<RecordType>, records: List<io.forus.me.android.data.entity.records.response.Record> ->
                    records.map {
                        val type = types.find { type -> type.key.equals(it.key) }
                        var category: RecordCategory? = null

                        if(it.recordCategoryId != null) category = categories.find { cat -> cat.id == it.recordCategoryId}
                        Record(it.id, it.value, it.order, type!!, category, it.valid ?: false,
                                it.validations.map {

                                    var organization: io.forus.me.android.domain.models.vouchers.Organization? = null
                                    if(it.organization != null)
                                    organization = Organization(it.organization.id,it.organization.name,null,null,null,
                                            null,it.organization.phone,it.organization.email)

                                    val validatorsList: MutableList<ValidatorOrganization> = mutableListOf()
                                    if(it.validators != null && it.validators.size > 0){
                                        for (validator in it.validators){
                                            val validationOrg = ValidatorOrganization(validator.id, validator.name)
                                            validatorsList.add(validationOrg)
                                        }
                                    }

                                    Validation(Validation.State.valueOf(it.state.toString()), it.identityAddress, it.createdAt, it.updatedAt,
                                            it.uuid, it.value, it.key, it.name,organization,validatorsList )
                                })
                    }
                }
        ).toObservable()
    }

    override fun getRecords(recordCategoryId: Long): Observable<List<Record>> {
        return Single.zip(
                Single.fromObservable(getCategory(recordCategoryId)),
                Single.fromObservable(getRecordTypes()),
                BiFunction { category : RecordCategory, types: List<RecordType> ->
                    recordsRemoteDataSource.getRecords(recordCategoryId)
                            .map{ list ->
                                list.map {
                                    val type = types.find { type -> type.key.equals(it.key) }
                                    Record(it.id, it.value, it.order, type!!, category, it.valid ?: false,
                                            it.validations.map {
                                                var organization: io.forus.me.android.domain.models.vouchers.Organization? = null
                                                if(it.organization != null)
                                                 organization = Organization(it.organization.id,it.organization.name,null,null,null,
                                                        null,it.organization.phone,it.organization.email)

                                                val validatorsList: MutableList<ValidatorOrganization> = mutableListOf()
                                                if(it.validators != null && it.validators.size > 0){
                                                    for (validator in it.validators){
                                                        val validationOrg = ValidatorOrganization(validator.id, validator.name)
                                                        validatorsList.add(validationOrg)
                                                    }
                                                }


                                                Validation(Validation.State.valueOf(it.state.toString()), it.identityAddress, it.createdAt, it.updatedAt,
                                                        it.uuid, it.value, it.key, it.name, organization, validatorsList) })
                                }
                            }
                }
        ).flatMapObservable {
            it
        }
    }

    override fun newRecord(model: NewRecordRequest): Observable<CreateRecordResponse> {
        val createRecord = io.forus.me.android.data.entity.records.request.CreateRecord(model.recordType?.key, model.category?.id, model.value, model.order)
        return recordsRemoteDataSource.createRecord(createRecord)
                .map{ CreateRecordResponse(it.id, it.value, it.order)}
        //.delay(100, TimeUnit.MILLISECONDS)
    }

    override fun getRecord(recordId: Long): Observable<Record> {
        return Single.zip(
                Single.fromObservable(recordsRemoteDataSource.retrieveRecord(recordId)),
                Single.fromObservable(getRecordTypes()),
                BiFunction { record : io.forus.me.android.data.entity.records.response.Record, types: List<RecordType> ->
                    val type = types.find { type -> type.key.equals(record.key) }
                    getCategory(record.recordCategoryId)
                            .map {
                                Record(record.id, record.value, record.order, type!!, it, record.valid ?: false,
                                        record.validations.map {
                                            var organization: io.forus.me.android.domain.models.vouchers.Organization? = null
                                            if(it.organization != null)
                                             organization  = Organization(it.organization.id,it.organization.name,null,null,null,
                                                    null,it.organization.phone,it.organization.email)

                                            val validatorsList: MutableList<ValidatorOrganization> = mutableListOf()
                                            if(it.validators != null && it.validators.size > 0){
                                                for (validator in it.validators){
                                                    val validationOrg = ValidatorOrganization(validator.id, validator.name)
                                                    validatorsList.add(validationOrg)
                                                }
                                            }

                                            Validation(Validation.State.valueOf(it.state.toString()), it.identityAddress, it.createdAt, it.updatedAt, it.uuid, it.value, it.key, it.name, organization, validatorsList) })
                            }
                }
        ).flatMapObservable {
            it
        }
    }

    override fun getRecordUuid(recordId: Long): Observable<String> {
        return recordsRemoteDataSource.createValidationToken(recordId)
                .map { it.uuid }
    }

    override fun readValidation(uuid: String): Observable<Validation> {
        return recordsRemoteDataSource.readValidation(uuid).map {
            var organization: io.forus.me.android.domain.models.vouchers.Organization? = null
            if(it.organization != null)
             organization  = Organization(it.organization.id,it.organization.name,null,null,null,
                    null,it.organization.phone,it.organization.email)

            val validatorsList: MutableList<ValidatorOrganization> = mutableListOf()
            if(it.validators != null && it.validators.size > 0){
                for (validator in it.validators){
                    val validationOrg = ValidatorOrganization(validator.id, validator.name)
                    validatorsList.add(validationOrg)
                }
            }

            Validation(Validation.State.valueOf(it.state.toString()), it.identityAddress, it.createdAt, it.updatedAt, it.uuid, it.value, it.key, it.name, organization, validatorsList) }
    }

    override fun approveValidation(uuid: String): Observable<Boolean> {
        return recordsRemoteDataSource.approveValidation(uuid).map { true }
    }

    override fun declineValidation(uuid: String): Observable<Boolean> {
        return recordsRemoteDataSource.declineValidation(uuid).map { true }
    }
}