package io.forus.me.android.domain.models.vouchers

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class Product(var id: Long?,
              var organizationId: Long?,
              var productCategoryId: Long?,
              var name: String?,
              var description: String?,
              var price: BigDecimal?,
              var oldPrice: BigDecimal?,
              var totalAmount: Long?,
              var soldAmount: Long?,
              var productCategory: ProductCategory?,
              var organization: Organization?)
