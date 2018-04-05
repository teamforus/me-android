package io.forus.me.entities

import android.arch.lifecycle.LiveData
import io.forus.me.services.IdentityService
import io.forus.me.services.RecordService

class RecordCategory (
        val id: Int,
        val labelResource: Int,
        val iconResource: Int,
        private var records: LiveData<List<Record>>? = RecordService.getRecordsByCategoryByIdentity(id, IdentityService.currentAddress)
)