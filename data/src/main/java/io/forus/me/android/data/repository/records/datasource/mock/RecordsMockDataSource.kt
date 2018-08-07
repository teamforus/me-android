package io.forus.me.android.data.repository.records.datasource.mock

import io.forus.me.android.data.entity.common.Success
import io.forus.me.android.data.entity.records.request.*
import io.forus.me.android.data.entity.records.response.Record
import io.forus.me.android.data.entity.records.response.RecordCategory
import io.forus.me.android.data.entity.records.response.RecordType
import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.reactivex.Observable
import io.reactivex.Single

class RecordsMockDataSource() : RecordsDataSource{

    val categories : MutableList<RecordCategory> = mutableListOf()
    val records : MutableList<Record> = mutableListOf()

    init {
        categories.add(RecordCategory(1, "Persoonlijk", 1, "https://psv4.userapi.com/c848332/u51841847/docs/d7/322ff10653f1/persoonlijk.png?extra=RStEZi_Qmom-Hw6_GjcDnba87UAbhAOyhBJ9XUuMka8PiJa7Xg_kn_ZIY_NOFTaRDlMBlkN7R-MYflpBY56Ux3MVBNGjqny4WoaLe9YkN4qEFDcDsMfsLTOHUR-XGg3_LycKk7HTBuI"))
        categories.add(RecordCategory(2, "Medish", 2, "https://vk.com/doc51841847_471254526?hash=18138fdd497b77a929&dl=700dcd9987bdc61121"))
        categories.add(RecordCategory(3, "Zakelijk", 3, "https://psv4.userapi.com/c848332/u51841847/docs/d8/9eab898d7d6e/zakelijk.png?extra=1AD6I8j2B6u4OWBqKerurTy4PDkDxhTDNhcXROVXd9M_GBeaNTsvLqazKls9rhJLBSRlJnLLDL3GqrtlJjMXH2vwqRTrRpPHoTGkOddR6N2D4bsCVjqsmtTcav7RCE8zgRlZsW7Y6wU"))
        categories.add(RecordCategory(4, "Relaties", 4, "https://psv4.userapi.com/c848332/u51841847/docs/d18/6f40922d507a/relaties.png?extra=_Y7BAq9JR_RTIi0esyeyCKXK6QzdpRYim1pueZ3qgtu4vzsumPjarbCxZ2T59FvzbxMwxEqvnstQEXrRod5UA5WvmFa92mJYpsK3c2PQ1i-q_sO_RlCrRHyc6qOx-XE6uT4rKSSlf48"))
        categories.add(RecordCategory(5, "Certificaten", 5, "https://psv4.userapi.com/c848332/u51841847/docs/d14/67ddb48f47cf/certificaten.png?extra=Dv3d6FLcyz2HYQ9gpnYI7PVXb3Rlngfq9K0guR0iaabd_bUlrif_MrJ9CLbInLJgaiZhdxOa_KsKVIaTB2zHhJDNELHDdEg30WsVCB9LBZAiWGhd49yYoOSF1QQ5pYZDH4_a6KHEivE"))
        categories.add(RecordCategory(6, "Anderen", 6,"https://psv4.userapi.com/c848332/u51841847/docs/d14/b67d50c67e51/anderen.png?extra=QEYpTPoB2Ec8ySyzGwu-Gbj2tbLFY9zchGoQLUKx_x5lVx3EPBWiK8Q8c9EduUTHzE9i5dV6iOAgXTE5Hg-NKvB08vjfLFOsDNfH7N3-riS0maDio0CApfl4FlJ4CksJ-Q2JEHH0KfI"))

        var counter: Long = 0
        for(category in categories){
            records.add(Record(counter++, "Jamal"+" ("+category.name+")", counter, "First Name", category.id, true, emptyList()))
            records.add(Record(counter++, "Vleij"+" ("+category.name+")", counter, "Last Name", category.id, true, emptyList()))
            records.add(Record(counter++, "45547646455"+" ("+category.name+")", counter, "BSN", category.id, true, emptyList()))
            records.add(Record(counter++, "jamal@forus.io"+" ("+category.name+")", counter, "E-mail", category.id, true, emptyList()))
            records.add(Record(counter++, "+1(234)567890"+" ("+category.name+")", counter, "Phone number", category.id, true, emptyList()))
            records.add(Record(counter++, "Male"+" ("+category.name+")", counter, "Gender", category.id, true, emptyList()))
        }
    }


    override fun getRecordTypes(): Observable<List<RecordType>> {
        throw UnsupportedOperationException("Not implemented")
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

    override fun getRecords(type: String): Observable<List<Record>> {
        return Single.just(records.toList().filter{it -> it.recordCategoryId == categories.find { it2 -> it2.name == type }?.id}).toObservable()
    }

    override fun createRecord(createRecord: CreateRecord): Observable<Success> {
        throw UnsupportedOperationException("Not implemented")
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

}
