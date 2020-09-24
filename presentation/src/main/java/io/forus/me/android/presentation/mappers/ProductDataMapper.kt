package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.vouchers.Organization
import io.forus.me.android.presentation.models.vouchers.Product
import io.forus.me.android.presentation.models.vouchers.ProductCategory
import java.math.BigDecimal
import io.forus.me.android.domain.models.vouchers.Product as ProductDomain

class ProductDataMapper : Mapper<ProductDomain, Product>() {
    override fun transform(domainModel: ProductDomain): Product =
            Product(domainModel.id ?: -1L, domainModel.organizationId
                    ?: -1L, domainModel.productCategoryId
                    ?: -1L, domainModel.name, domainModel.description, domainModel.price ?: BigDecimal(-1),
                    domainModel.oldPrice ?: BigDecimal(-1), domainModel.totalAmount
                    ?: -1L, domainModel.soldAmount ?: -1L,
                    if (domainModel.productCategory != null) {
                        ProductCategory(domainModel.productCategory!!.id, domainModel.productCategory!!.key
                                ?: "",
                                domainModel.productCategory!!.name ?: "")
                    } else {
                        null
                    },
                    if (domainModel.organization != null) {
                        Organization(domainModel.organization!!.id,
                                domainModel.organization!!.name ?: "",
                                domainModel.organization!!.logo ?: "",
                                domainModel.organization!!.lat
                                        ?: 0f.toDouble(), domainModel.organization!!.lon
                                ?: 0f.toDouble(),
                                domainModel.organization!!.address
                                        ?: "", domainModel.organization!!.phone ?: "",
                                domainModel.organization!!.email ?: "")
                    } else {
                        null
                    })
}