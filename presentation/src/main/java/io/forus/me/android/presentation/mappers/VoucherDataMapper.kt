package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.vouchers.Transaction
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.domain.models.vouchers.Voucher as VoucherDomain


class VoucherDataMapper(private val currencyDataMapper: CurrencyDataMapper,
                        private val transactionDataMapper: TransactionDataMapper,
                        private val productDataMapper: ProductDataMapper) : Mapper<VoucherDomain, Voucher>() {
    override fun transform(domainModel: VoucherDomain): Voucher {
        if (domainModel == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }

        return with(domainModel) {
            Voucher(isProduct, isUsed, address, name, organizationName,
                    fundName, description, createdAt,
                    currencyDataMapper.transform(currency), amount, logo,
                    transactionDataMapper.transform(transactions) as List<Transaction>,
                    if (product != null) {
                        productDataMapper.transform(product!!)
                    } else {
                        null
                    })
        }
    }


}