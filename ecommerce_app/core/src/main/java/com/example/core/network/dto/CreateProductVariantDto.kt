package com.example.core.network.dto

import com.example.core.network.kSerializeChanger.UUIDKserialize
import java.util.UUID
import kotlinx.serialization.Serializable


@Serializable()
data class ProductVariantDto(
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    val name: String,
    val percentage: Int,
    @Serializable(with = UUIDKserialize::class)
    val variantId: UUID,
)

@Serializable
data class CreateProductVariantDto(
    val Name: String,
    val Percentage: Int?,
    @Serializable(with= UUIDKserialize::class)
    val VariantId: UUID,
)
