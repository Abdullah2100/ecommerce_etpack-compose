package com.example.core.network.dto


import com.example.core.network.kSerializeChanger.UUIDKserialize
import com.example.core.network.kSerializeChanger.UUIDListKserialize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class OrderItemDto(
    @Serializable(with = UUIDKserialize::class)
    val id: UUID,
    @Serializable(with = UUIDKserialize::class)
    val orderId: UUID,
    val price: Long,
    val quantity: Int,
    val address: List<AddressWithTitleDto>? = null,
    val product: OrderProductDto? = null,
    val productVariant: List<OrderVariantDto>? = null,
    val orderItemStatus: String,
    val orderStatusName: String
)


@Serializable
data class CreateOrderItemDto(
    @Serializable(with = UUIDKserialize::class)
    val StoreId: UUID,
    val Price: Int,
    val Quantity: Int,
    @Serializable(with = UUIDKserialize::class)
    val ProductId: UUID,
    @Serializable(with = UUIDListKserialize::class)
    val ProductsVariantId: List<UUID>,
)


@Serializable()
data class OrderItemsStatusEvent(
    @Serializable(with = UUIDKserialize::class)
    val orderId: UUID,
    @Serializable(with = UUIDKserialize::class)
    val orderItemId: UUID,
    val status: String
)

@Serializable
data class UpdateOrderItemStatusDto(
    @Serializable(with = UUIDKserialize::class)
    val Id: UUID,
    val Status: Int
)
