package io.forus.me.services

import android.arch.lifecycle.LiveData
import io.forus.me.R
import io.forus.me.entities.Record
import io.forus.me.entities.RecordCategory
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.base.BaseService
import io.forus.me.services.base.EthereumItemService

/**
 * Created by martijn.doornik on 27/02/2018.
 */
class RecordService : EthereumItemService<Record>() {

    override fun add(item: Record) {
        addRecord(item)
    }

    override fun delete(item: Record) {
        deleteRecord(item)
    }

    override fun getItem(address: String): Record {
        return DatabaseService.inject.recordDao().getRecord(address)
    }

    override fun getList(identity: String): List<Record> {
        return DatabaseService.inject.recordDao().getRecords(identity)
    }

    override fun getLiveData(identity: String): LiveData<List<Record>> {
        return DatabaseService.inject.recordDao().getRecordsLiveData(identity)
    }

    override fun getLiveItem(address: String): LiveData<Record> {
        return DatabaseService.inject.recordDao().getLiveRecord(address)
    }

    override fun getThread(): ThreadHelper.DataThread {
        return thread
    }

    override fun update(item: Record) {
        updateRecord(item)
    }

    class CategoryIdentifier {
        companion object {
            val PERSONAL: Int = 0
            val LICENCES: Int = 1
            val MEDICAL: Int = 2
            val PROFESSIONAL: Int = 3
            val RELATIONS: Int = 4
            val OTHER: Int = 5
            val list: List<Int>
                get() = listOf(PERSONAL, LICENCES, MEDICAL, PROFESSIONAL, RELATIONS, OTHER)

            fun nameOf(id: Int): String {
                // TODO FIX HARDCODING
                when (id) {
                    PERSONAL -> return "Persoonlijk"
                    LICENCES -> return "Licenties"
                    MEDICAL -> return "Medisch"
                    PROFESSIONAL -> return "Professioneel"
                    RELATIONS -> return "Relaties"
                    OTHER -> return "Overig"
                }
                return ""
            }

        }

    }

    companion object {
        private var _categories:List<RecordCategory> = listOf(
                RecordCategory(CategoryIdentifier.PERSONAL, R.string.record_personal, R.drawable.ic_person_black_24dp),
                RecordCategory(CategoryIdentifier.LICENCES, R.string.record_licences, R.drawable.ic_licences_24dp),
                RecordCategory(CategoryIdentifier.MEDICAL, R.string.record_medical, R.drawable.ic_medical_24dp),
                RecordCategory(CategoryIdentifier.PROFESSIONAL, R.string.record_professional, R.drawable.ic_professional_24dp),
                RecordCategory(CategoryIdentifier.RELATIONS, R.string.record_relations, R.drawable.ic_relations_24dp),
                RecordCategory(CategoryIdentifier.OTHER, R.string.record_other, R.drawable.ic_other_24dp)
                )
        private val thread: ThreadHelper.DataThread
                get() = ThreadHelper.dispense(ThreadHelper.RECORD_THREAD)

        fun addRecord(record: Record) {
            thread.postTask(Runnable { DatabaseService.inject.recordDao().insert(record) })
        }

        fun deleteRecord(record: Record) {
            thread.postTask(Runnable { DatabaseService.inject.recordDao().delete(record) })

        }

        fun getRecordsByCategoryByIdentity(category: Int, identity: String): LiveData<List<Record>>? {
            return DatabaseService.database?.recordDao()?.getRecordsFromCategoryAndIdentity(category, identity)
        }

        fun getRecordCategoriesByIdentity(identity:String = IdentityService.currentAddress): List<RecordCategory> {
            return _categories
        }

        /**
         * Create a new record to track.
         *
         * @param address the validated address
         * @param name the name of the record
         * @param category The category of the record
         */
        fun newRecord(address: String, name: String, category: RecordCategory) {
            thread.postTask(Runnable { DatabaseService.inject.recordDao()
                    .insert(
                            Record(address, name, category.id)
                    ) })
        }

        fun updateRecord(record:Record) {
            thread.postTask(Runnable { DatabaseService.inject.recordDao().update(record) })
        }
    }
}