package io.forus.me.android.data.repository.records

import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.forus.me.android.domain.models.records.*
import io.reactivex.Observable
import io.reactivex.Single

class RecordsRepository(private val recordsRemoteDataSource: RecordsDataSource) : io.forus.me.android.domain.repository.records.RecordsRepository {

    override fun getRecordTypes(): Observable<List<RecordType>> {
        return recordsRemoteDataSource.getRecordTypes()
                .map { it.map { RecordType(it.key, it.type, it.name) }}
    }

    override fun getCategories(): Observable<List<RecordCategory>> {
        val categories : MutableList<RecordCategory> = mutableListOf()
        categories.add(RecordCategory(1, "Persoonlijk", 1, "https://psv4.userapi.com/c848332/u51841847/docs/d7/322ff10653f1/persoonlijk.png?extra=RStEZi_Qmom-Hw6_GjcDnba87UAbhAOyhBJ9XUuMka8PiJa7Xg_kn_ZIY_NOFTaRDlMBlkN7R-MYflpBY56Ux3MVBNGjqny4WoaLe9YkN4qEFDcDsMfsLTOHUR-XGg3_LycKk7HTBuI"))
        categories.add(RecordCategory(2, "Medish", 2, "https://vk.com/doc51841847_471254526?hash=18138fdd497b77a929&dl=700dcd9987bdc61121"))
        categories.add(RecordCategory(3, "Zakelijk", 3, "https://psv4.userapi.com/c848332/u51841847/docs/d8/9eab898d7d6e/zakelijk.png?extra=1AD6I8j2B6u4OWBqKerurTy4PDkDxhTDNhcXROVXd9M_GBeaNTsvLqazKls9rhJLBSRlJnLLDL3GqrtlJjMXH2vwqRTrRpPHoTGkOddR6N2D4bsCVjqsmtTcav7RCE8zgRlZsW7Y6wU"))
        categories.add(RecordCategory(4, "Relaties", 4, "https://psv4.userapi.com/c848332/u51841847/docs/d18/6f40922d507a/relaties.png?extra=_Y7BAq9JR_RTIi0esyeyCKXK6QzdpRYim1pueZ3qgtu4vzsumPjarbCxZ2T59FvzbxMwxEqvnstQEXrRod5UA5WvmFa92mJYpsK3c2PQ1i-q_sO_RlCrRHyc6qOx-XE6uT4rKSSlf48"))
        categories.add(RecordCategory(5, "Certificaten", 5, "https://psv4.userapi.com/c848332/u51841847/docs/d14/67ddb48f47cf/certificaten.png?extra=Dv3d6FLcyz2HYQ9gpnYI7PVXb3Rlngfq9K0guR0iaabd_bUlrif_MrJ9CLbInLJgaiZhdxOa_KsKVIaTB2zHhJDNELHDdEg30WsVCB9LBZAiWGhd49yYoOSF1QQ5pYZDH4_a6KHEivE"))
        categories.add(RecordCategory(6, "Anderen", 6,"https://psv4.userapi.com/c848332/u51841847/docs/d14/b67d50c67e51/anderen.png?extra=QEYpTPoB2Ec8ySyzGwu-Gbj2tbLFY9zchGoQLUKx_x5lVx3EPBWiK8Q8c9EduUTHzE9i5dV6iOAgXTE5Hg-NKvB08vjfLFOsDNfH7N3-riS0maDio0CApfl4FlJ4CksJ-Q2JEHH0KfI"))

        return Single.just(categories.toList()).toObservable()
    }

    override fun newCategory(model: NewRecordCategoryRequest): Observable<Boolean> {
        return recordsRemoteDataSource.createRecordCategory(io.forus.me.android.data.entity.records.request.CreateCategory(model.order, model.name))
                .map { true }
    }

    override fun getCategory(id: Long): Observable<RecordCategory> {
        return recordsRemoteDataSource.retrieveRecordCategory(id)
                .map{ RecordCategory(it.id, it.name ?: "noname", it.order, "")}
    }

    override fun getRecords(): Observable<List<Record>> {
        val records : MutableList<Record> = mutableListOf()

        val personal = RecordCategory(1, "Personal", 1)
        val medical = RecordCategory(2, "Medical", 2)
        val professional = RecordCategory(3, "Professional", 3)

        for (i in 0..6) {
            records.add(Record(i.toString(), "Title", "Value", personal))
        }


        for (i in 0..6) {
            records.add(Record((i*10).toString(), "Title", "Value", medical))
        }

        for (i in 0..6) {
            records.add(Record((i*100).toString(), "Title", "Value", professional))
        }

        return Single.just(records.toList()).toObservable()
    }

    override fun newRecord(model: NewRecordRequest): Observable<NewRecordRequest> {
        val createRecord = io.forus.me.android.data.entity.records.request.CreateRecord(model.recordType?.key, model.category?.id, model.value, model.order)
        return recordsRemoteDataSource.createRecord(createRecord)
                .flatMap {
                    Observable.just(model)
                }
    }

    override fun getRecord(id: Long): Observable<Record> {
        return recordsRemoteDataSource.retrieveRecord(id)
                .map{ Record(if(it.id != null) it.id.toString() else "???", it.key ?: "key", it.value ?: "value", RecordCategory(it.recordCategoryId ?: -1, "", 0, ""))}
    }
}