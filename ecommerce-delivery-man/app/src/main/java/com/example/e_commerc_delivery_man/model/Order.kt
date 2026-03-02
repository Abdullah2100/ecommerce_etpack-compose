package com.example.e_commerc_delivery_man.model

import com.example.eccomerce_app.model.OrderItem
import java.util.UUID

data class Order(
    val id: UUID,
    val longitude: Double,
    val latitude: Double,
    val status: String,
    val userPhone: String,
    var name: String,
    var symbol: String,
    val totalPrice: Double,
    val realPrice: Double? = null,
    val deliveryFee: Double,
    val isAlreadyPayed: Boolean,
    val orderItems: List<OrderItem>
)
