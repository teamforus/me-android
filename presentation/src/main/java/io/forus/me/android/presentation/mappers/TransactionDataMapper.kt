package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.vouchers.Transaction
import io.forus.me.android.domain.models.vouchers.Transaction as TransactionDomain

class TransactionDataMapper(private val currencyDataMapper: CurrencyDataMapper,
                            private val organizationDataMapper: OrganizationDataMapper) : Mapper<TransactionDomain, Transaction>() {

    override fun transform(domainModel: TransactionDomain): Transaction =
            Transaction(domainModel.id, organizationDataMapper.transform(domainModel.organization),
                    currencyDataMapper.transform(domainModel.currency),
                    domainModel.amount, domainModel.createdAt,
                    Transaction.Type.valueOf(domainModel.type.name))
}