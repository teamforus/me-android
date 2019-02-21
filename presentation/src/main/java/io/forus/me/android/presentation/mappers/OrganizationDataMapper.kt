package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.vouchers.Organization
import io.forus.me.android.domain.models.vouchers.Organization as OrganizationDomain

class OrganizationDataMapper : Mapper<OrganizationDomain, Organization>() {

    override fun transform(domainModel: OrganizationDomain) =
            Organization(domainModel.id, domainModel.name ?: "", domainModel.logo ?: "")
}