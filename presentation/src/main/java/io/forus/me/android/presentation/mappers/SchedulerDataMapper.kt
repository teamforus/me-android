package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.vouchers.Schedule
import io.forus.me.android.domain.models.vouchers.Schedule as ScheduleDomain

class SchedulerDataMapper : Mapper<ScheduleDomain, Schedule>() {

    override fun transform(domainModel: ScheduleDomain) =
            Schedule(domainModel.id, domainModel.officeId ?: -1L , domainModel.weekDay ?: 0,
            domainModel.startTime ?: "", domainModel.endTime ?:"" )
}