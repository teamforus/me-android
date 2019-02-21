package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.vouchers.Transaction
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.domain.models.vouchers.Voucher as VoucherDomain


class VoucherDataMapper(private val currencyDataMapper: CurrencyDataMapper,
                        private val transactionDataMapper: TransactionDataMapper,
                        private val productDataMapper: ProductDataMapper) : Mapper<VoucherDomain, Voucher>() {
    override fun transform(domainModel: VoucherDomain): Voucher {

        return with(domainModel) {
            Voucher(isProduct ?: false,
                    isUsed ?: false,
                    address ?: "",
                    name ?: "",
                    organizationName ?: "",
                    fundName ?: "",
                    fundWebShopUrl ?: "",
                    description ?: "",
                    createdAt,
                    currencyDataMapper.transform(currency),
                    amount ?: 0f.toBigDecimal(),
                    logo,
                    transactionDataMapper.transform(transactions) as List<Transaction>,
                    if (product != null) {
                        productDataMapper.transform(product!!)
                    } else {
                        null
                    })
        }
    }


}