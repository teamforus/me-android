package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.currency.Currency
import io.forus.me.android.domain.models.currency.Currency as CurrencyDomain

class CurrencyDataMapper: Mapper<CurrencyDomain?, Currency?>() {

    override fun transform(domainModel: CurrencyDomain?): Currency? =
           if (domainModel != null) Currency(domainModel.name, domainModel.logoUrl) else null
}