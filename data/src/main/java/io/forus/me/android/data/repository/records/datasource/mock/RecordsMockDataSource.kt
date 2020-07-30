package io.forus.me.android.data.repository.records.datasource.mock

import io.forus.me.android.data.entity.common.Success
import io.forus.me.android.data.entity.records.request.*
import io.forus.me.android.data.entity.records.response.*
import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.reactivex.Observable
import io.reactivex.Single

class RecordsMockDataSource : RecordsDataSource{


    val types: MutableList<RecordType> = mutableListOf()
    val categories : MutableList<RecordCategory> = mutableListOf()
    val records : MutableList<Record> = mutableListOf()
    var recordCreateCounter: Long = 0

    init {
        types.add(RecordType("primary_email", "string", "Primary e-mail"))
        types.add(RecordType("email", "string", "E-mail"))
        types.add(RecordType("given_name", "string", "Given Name"))
        types.add(RecordType("family_name", "string", "Family Name"))
        types.add(RecordType("children", "string", "Children"))
        types.add(RecordType("parent", "string", "Parent"))
        types.add(RecordType("address", "string", "Address"))
        types.add(RecordType("birth_date", "string", "Birth date"))
        types.add(RecordType("gender", "string", "Gender"))
        types.add(RecordType("spouse", "string", "Spouse"))
        types.add(RecordType("tax_id", "string", "Tax ID"))
        types.add(RecordType("telephone", "string", "Telephone"))
        types.add(RecordType("net_worth", "number", "Net worth"))
        types.add(RecordType("base_salary", "number", "Base salary"))
        types.add(RecordType("bsn", "number", "BSN"))

        categories.add(RecordCategory(1, "Persoonlijk", 1, "https://test.platform.forus.io/assets/category-icons/persoonlijk.png"))
        categories.add(RecordCategory(2, "Medical", 2, "https://test.platform.forus.io/assets/category-icons/medical.png"))
        categories.add(RecordCategory(3, "Zakelijk", 3, "https://test.platform.forus.io/assets/category-icons/zakelijk.png"))
        categories.add(RecordCategory(4, "Relaties", 4, "https://test.platform.forus.io/assets/category-icons/relaties.png"))
        categories.add(RecordCategory(5, "Certificaten", 5, "https://test.platform.forus.io/assets/category-icons/certificaten.png"))
        categories.add(RecordCategory(6, "Anderen", 6,"https://test.platform.forus.io/assets/category-icons/anderen.png"))

        var counter2: Long = 0
        for(category in categories){
            records.add(Record(recordCreateCounter++, "Jamal"+" ("+category.name+")", recordCreateCounter, "given_name", category.id, true, emptyList()))
            records.add(Record(recordCreateCounter++, "Vleij", recordCreateCounter, "family_name", category.id, true, emptyList()))
            records.add(Record(recordCreateCounter++, "45547646455", recordCreateCounter, "bsn", category.id, true, emptyList()))
            records.add(Record(recordCreateCounter++, "jamal@forus.io", recordCreateCounter, "primary_email", category.id, true, emptyList()))
            if(counter2 % 2L == 0L) records.add(Record(recordCreateCounter++, "jamal2@forus.io", recordCreateCounter, "email", category.id, true, emptyList()))
            if(counter2 % 3L == 0L) records.add(Record(recordCreateCounter++, "+1(234)567890", recordCreateCounter, "telephone", category.id, true, emptyList()))
            if(counter2 % 5L == 0L) records.add(Record(recordCreateCounter++, "Male", recordCreateCounter, "gender", category.id, true, emptyList()))
            counter2++
        }
    }


    override fun getRecordTypes(): Observable<List<RecordType>> {
        return Single.just(types.toList()).toObservable()
    }

    override fun getRecordCategories(): Observable<List<RecordCategory>> {
        return Single.just(categories.toList()).toObservable()
    }

    override fun createRecordCategory(createCategory: CreateCategory): Observable<Success> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun retrieveRecordCategory(id: Long): Observable<RecordCategory> {
        val recordCategory: RecordCategory? = categories.find{it -> it.id == id}
        if(recordCategory != null) return Single.just(recordCategory).toObservable()
        else return Observable.error(Exception("Not found"))
    }

    override fun updateRecordCategory(id: Long, updateCategory: UpdateCategory): Observable<Success> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun deleteRecordCategory(id: Long): Observable<Success> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun sortRecordCategories(sortCategories: SortCategories): Observable<Success> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun getRecords(): Observable<List<Record>> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun getRecordsArchived(): Observable<List<Record>> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun getRecords(categoryId: Long): Observable<List<Record>> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun getRecords(type: String): Observable<List<Record>> {
        return Single.just(records.toList().filter{it -> it.recordCategoryId == categories.find { it2 -> it2.name == type }?.id}).toObservable()
    }

    override fun createRecord(createRecord: CreateRecord): Observable<Record> {
        val record = Record(recordCreateCounter++, createRecord.value, recordCreateCounter, createRecord.key, createRecord.recordCategoryId, false, emptyList())
        records.add(record)
        return Single.just(record).toObservable()
    }

    override fun retrieveRecord(id: Long): Observable<Record> {
        val record: Record? = records.find{it -> it.id == id}
        if(record != null) return Single.just(record).toObservable()
        else return Observable.error(Exception("Not found"))
    }

    override fun updateRecord(id: Long, updateRecord: UpdateRecord): Observable<Success> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun deleteRecord(id: Long): Observable<Success> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun sortRecords(sortRecords: SortRecords): Observable<Success> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun createValidationToken(recordId: Long): Observable<ValidationToken> {
        return Observable.just(ValidationToken(java.util.UUID.randomUUID().toString()))
    }

    override fun readValidation(uuid: String): Observable<Validation> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun approveValidation(uuid: String,validateRecord: ValidateRecord): Observable<Success> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun declineValidation(uuid: String): Observable<Success> {
        throw UnsupportedOperationException("Not implemented")
    }
}
