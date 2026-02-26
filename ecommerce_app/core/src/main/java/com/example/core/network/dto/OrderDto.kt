package com.example.core.network.dto

import com.example.core.network.kSerializeChanger.UUIDKserialize
import com.example.core.network.kSerializeChanger.UUIDListKserialize
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
    var isAlreadyPayed: Boolean,
    val status:String,
    val symbol:String,
    val totalPrice: Long,
    val deliveryFee: Int,
    val orderItems:List<OrderItemDto>
)

@Serializable
data class OrderUpdateStatusDto(
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    val status:String,
)
@Serializable
data class CreateOrderDto(
    val Longitude: Double,
    val Latitude: Double,
    val TotalPrice: Long,
    val Symbol: String,
    @Serializable(with = UUIDKserialize::class)
    val PaymentTypeId: UUID,
    val Items: List<CreateOrderItemDto>,
    val PaymentId: String?,
)

@Serializable
data class OrderRequestItemsDto(
    @Serializable(with = UUIDKserialize::class)
    val StoreId: UUID,
    val Price: Long,
    val Quantity: Int,
    @Serializable(with = UUIDKserialize::class)
    val ProductId: UUID,
    @Serializable(with = UUIDListKserialize::class)
    val ProductsVariantId: List<UUID>,
)
