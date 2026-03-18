package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.vouchers.Transaction
import io.forus.me.android.domain.models.vouchers.Transaction as TransactionDomain

class TransactionDataMapper(
    private val organizationDataMapper: OrganizationDataMapper,
    private val productDataMapper: ProductDataMapper,
) : Mapper<TransactionDomain, Transaction>() {
    override fun transform(domainModel: TransactionDomain): Transaction =
        Transaction(
            domainModel.id,
            if (domainModel.organization != null) organizationDataMapper.transform(domainModel.organization!!) else null,
            domainModel.amount ?: 0f.toBigDecimal(),
            domainModel.amount_locale,
            domainModel.amount_extra_cash ?: 0f.toBigDecimal(),
            domainModel.amount_extra_cash_locale,
            domainModel.createdAt,
            Transaction.Type.valueOf(domainModel.type.name),
            if (domainModel.product != null) productDataMapper.transform(domainModel.product!!) else null
        )
}
