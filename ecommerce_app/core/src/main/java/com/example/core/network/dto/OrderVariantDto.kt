package com.example.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderVariantDto(
    val variantName:String,
    val name:String
)