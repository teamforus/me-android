package io.forus.me.android.presentation.mappers

import io.forus.me.android.presentation.models.vouchers.Organization
import io.forus.me.android.presentation.models.vouchers.Product
import io.forus.me.android.presentation.models.vouchers.ProductCategory
import io.forus.me.android.domain.models.vouchers.Product as ProductDomain

class ProductDataMapper: Mapper<ProductDomain, Product>() {
    override fun transform(domainModel: ProductDomain): Product =
            Product(domainModel.id, domainModel.organizationId, domainModel.productCategoryId, domainModel.name, domainModel.description, domainModel.price, domainModel.oldPrice, domainModel.totalAmount, domainModel.soldAmount, ProductCategory(domainModel.productCategory.id, domainModel.productCategory.key ?: "", domainModel.productCategory.name ?: ""),
                    Organization(domainModel.organization.id,
                    domainModel.organization.name ?: "",
                            domainModel.organization.logo ?: "",
                            domainModel.organization.lat ?: 0f.toDouble(), domainModel.organization.lon ?: 0f.toDouble(), domainModel.organization.address ?: "", domainModel.organization.phone ?: "", domainModel.organization.email ?: ""))
}