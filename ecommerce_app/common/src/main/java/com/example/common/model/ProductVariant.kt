package com.example.common.model

import java.util.UUID

data class ProductVariant(
    val id: UUID,
    val name:String,
    val percentage: Int,
    val variantId: UUID,
    )
