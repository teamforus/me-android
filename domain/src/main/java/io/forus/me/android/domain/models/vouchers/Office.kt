package io.forus.me.android.domain.models.vouchers

class Office(var id: Long,
             var organizationId: Long?,
             var address: String?,
             var phone: String?,
             var lat: Double?,
             var lon: Double?,
             var photo: String?,
             var organization: Organization?,
             var schedulers: List<Schedule>?)