package com.example.common.model

import java.util.UUID

data class Order(
    val id: UUID,
    val longitude: Double,
    val latitude: Double,
    val status:String,
    val userPhone: String,
    var name: String,
    var symbol: String,
    var isAlreadyPayed: Boolean,
    val totalPrice: Long,
    val deliveryFee: Int,
    val orderItems:List<OrderItem>
)
