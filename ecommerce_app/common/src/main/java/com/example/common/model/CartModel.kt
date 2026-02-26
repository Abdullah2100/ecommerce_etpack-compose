package com.example.common.model

import java.util.UUID

data class CartModel(
    val totalPrice: Long,
    val longitude: Double,
    val latitude: Double,
    val userId: UUID,
    val cartProducts:List<CardProductModel>
)