package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.vouchers.Office
import io.forus.me.android.presentation.models.vouchers.Transaction
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.domain.models.vouchers.Voucher as VoucherDomain


class VoucherDataMapper(private val currencyDataMapper: CurrencyDataMapper,
                        private val transactionDataMapper: TransactionDataMapper,
                        private val productDataMapper: ProductDataMapper,
                        private val officeDataMapper: OfficeDataMapper) : Mapper<VoucherDomain, Voucher>() {
    override fun transform(domainModel: VoucherDomain): Voucher {

        return with(domainModel) {
            Voucher(isProduct ?: false,
                    isUsed ?: false,
                    address ?: "",
                    identyAddress?:"",
                    name ?: "",
                    organizationName ?: "",
                    fundName ?: "",
                    fundType ?: "",
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
                    },
                    deactivated ?: false,
                    expired ?: false,
                    expireDate ?: "",
                    officeDataMapper.transform(offices) as List<Office>
            )
        }
    }


}