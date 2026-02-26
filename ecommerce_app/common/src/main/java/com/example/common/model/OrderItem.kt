package com.example.common.model

import java.util.UUID


data class OrderItem(
    val id: UUID,
    val price: Long,
    val quantity:Int,
    val address: List<AddressWithTitle>?=null,
    val product: OrderProduct?=null,
    val productVariant:List<OrderVariant>?=null,
    val orderItemStatus: String,
    val orderStatusName: String,
    val orderId: UUID,
)