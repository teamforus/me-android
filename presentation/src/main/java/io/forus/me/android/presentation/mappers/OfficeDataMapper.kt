package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.vouchers.Office
import io.forus.me.android.presentation.models.vouchers.Organization
import io.forus.me.android.domain.models.vouchers.Office as OfficeDomain

class OfficeDataMapper : Mapper<OfficeDomain, Office>() {

    override fun transform(domainModel: OfficeDomain) =
            Office(domainModel.id, domainModel.organizationId , domainModel.address ?: "",
            domainModel.phone ?: "", domainModel.lat , domainModel.lon , domainModel.photo,
                    if (domainModel.organization != null) {
                        Organization(domainModel.organization!!.id,
                                domainModel.organization!!.name ?: "",
                                domainModel.organization!!.logo ?: "",
                                domainModel.organization!!.lat
                                        ?: 0f.toDouble(), domainModel.organization!!.lon
                                ?: 0f.toDouble(),
                                domainModel.organization!!.address
                                        ?: "", domainModel.organization!!.phone ?: "",
                                domainModel.organization!!.email ?: "")
                    } else {
                        null
                    })
}