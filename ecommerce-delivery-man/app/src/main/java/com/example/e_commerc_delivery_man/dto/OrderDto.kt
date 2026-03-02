package com.example.eccomerce_app.dto.response

import com.example.e_commerc_delivery_man.dto.OrderItemDto
import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class OrderDto(
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    val longitude: Double,
    val latitude: Double,
    val userPhone: String,
    var name: String,
    val status:String,
    val symbol:String,
    val totalPrice: Double,
    val deliveryFee: Double,
    val isAlreadyPayed: Boolean,
    val orderItems:List<OrderItemDto>
)

@Serializable
data class OrderUpdateStatusDto(
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    val status:String,
)
@Serializable
data class OrderUpdateEvent(
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    @Serializable(with = UUIDKserialize::class)
    val deliveryId: UUID,
)

@Serializable
data class UpdateOrderStatus(
    @Serializable(with = UUIDKserialize::class)
    val Id: UUID,
    val Status:Int
)
