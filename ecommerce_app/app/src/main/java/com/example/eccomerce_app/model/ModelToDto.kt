package com.example.common.model

import com.example.common.model.CardProductModel
import com.example.common.model.ProductVariant
import com.example.common.model.ProductVariantSelection
import com.example.common.model.SubCategoryUpdate
import com.example.core.network.dto.CreateOrderItemDto
import com.example.core.network.dto.CreateProductVariantDto
import com.example.core.network.dto.UpdateSubCategoryDto

object ModelToDto {



    fun SubCategoryUpdate.toUpdateSubCategoryDto(): UpdateSubCategoryDto {
        return UpdateSubCategoryDto(
            name = this.name,
            id = this.id,
            categoryId = this.cateogyId
        )
    }

    fun ProductVariantSelection.toProductVariantRequestDto(): CreateProductVariantDto {
        return CreateProductVariantDto(
            Name = this.name,
            Percentage = this.percentage,
            VariantId = this.variantId
        )
    }
    fun List<List<ProductVariant>>.toListOfProductVariant(): List<ProductVariantSelection> {
        return   this.map{it->it.map {
                data->
            ProductVariantSelection(name = data.name, percentage = data.percentage, variantId = data.variantId)
        }}.flatten()
    }


    fun CardProductModel.toOrderRequestItemDto(): CreateOrderItemDto {
        return CreateOrderItemDto(
            StoreId = this.storeId,
            ProductId = this.productId,
            Price = this.price,
            Quantity = this.quantity,
            ProductsVariantId = this.productVariants.map { it.id }
        )
    }

    fun SubCategoryUpdate.toSubCategoryUpdateDto(): UpdateSubCategoryDto {
        return UpdateSubCategoryDto(
            id = this.id,
            categoryId = this.cateogyId,
            name = this.name,
        )
    }

}