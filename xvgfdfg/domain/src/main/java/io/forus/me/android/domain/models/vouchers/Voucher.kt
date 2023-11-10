package io.forus.me.android.domain.models.vouchers

import io.forus.me.android.domain.models.currency.Currency
import java.math.BigDecimal
import java.util.*

class Voucher(var isProduct: Boolean?, var isUsed: Boolean?, var address: String?, var identyAddress: String?, var name: String?, var organizationName: String?, var fundName: String?, var fundType: String?, var fundWebShopUrl: String?,
              var description: String?, var createdAt: Date?, var currency: Currency?, var amount: BigDecimal?, var logo: String, var transactions: List<Transaction>, var product: Product? = null,
              val deactivated: Boolean?, val expired: Boolean?, val expireDate: String?, val offices: List<Office>) {


}
