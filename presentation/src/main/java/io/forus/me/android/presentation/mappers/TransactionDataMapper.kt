package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.vouchers.Transaction
import io.forus.me.android.domain.models.vouchers.Transaction as TransactionDomain

class TransactionDataMapper(private val currencyDataMapper: CurrencyDataMapper,
                            private val organizationDataMapper: OrganizationDataMapper) : Mapper<TransactionDomain, Transaction>() {

    override fun transform(domainModel: TransactionDomain): Transaction =
            Transaction(domainModel.id,
                    if (domainModel.organization != null) organizationDataMapper.transform(domainModel.organization!!) else null,
                    if (domainModel.currency != null) currencyDataMapper.transform(domainModel.currency) else null,
                    domainModel.amount ?: 0f.toBigDecimal(), domainModel.createdAt,
                    Transaction.Type.valueOf(domainModel.type.name))
}