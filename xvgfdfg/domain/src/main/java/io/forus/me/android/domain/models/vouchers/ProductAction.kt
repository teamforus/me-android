package io.forus.me.android.domain.models.vouchers

import io.forus.me.android.domain.models.currency.Currency
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

class ProductAction(var id: Long?, var name: String?, var organizationId: Long?,
                    var price: BigDecimal?, var priceUser: BigDecimal?,
                    var priceType: String?,var priceDiscount: BigDecimal?,
                    var priceLocale: String?, var priceUserLocale: String?,
                    var sponsorSubsidy: BigDecimal?,var photoURL: String?,
                    var organization: Organization?, var sponsor: Organization?,
                    var productCategory: ProductCategory?) : Serializable {


}
