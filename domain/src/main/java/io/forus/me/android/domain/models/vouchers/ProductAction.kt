package io.forus.me.android.domain.models.vouchers

import io.forus.me.android.domain.models.currency.Currency
import java.math.BigDecimal
import java.util.*

class ProductAction(var id: Long? , var name: String?, var organizationId: Long?,  var price: BigDecimal?, var priceUser: BigDecimal?,
  var photoURL: String?   , var organization: Organization?, var productCategory: ProductCategory?) {


}
